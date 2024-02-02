package com.capital7software

import java.nio.file.Files
import java.nio.file.Paths
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JavaConventionPluginTest : PluginTest() {

  @BeforeEach
  fun init() {
    buildFile.appendText("""
            plugins {
                id("capital7software.java-conventions")
            }
        """)
  }

  @Test
  fun `fails on checkstyle error`() {
    Files
        .createDirectories(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software"
            )
        )
        .toFile()
    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software",
                "Foo.java"
            )
        )
        .toFile()
        .appendText("""
package com.capital7software;

import java.util.*;

/**
 * The Foo class.
 **/
public class Foo {
  /**
   * The default constructor.
   **/
  public Foo() {
  }
  
  /**
   * The bar method.
   **/
  public void bar() {
  }
}
        """)

    val result = runTaskWithFailure("build")

    assertEquals(TaskOutcome.FAILED, result.task(":checkstyleMain")?.outcome)
    assertTrue(result.output.contains("Checkstyle rule violations were found."))
    assertTrue(result.output.contains("Checkstyle violations by severity: [error:1]"))
  }

  @Test
  fun `fails on checkstyle warning`() {
    Files
        .createDirectories(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software"
            )
        )
        .toFile()
    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software",
                "Foo.java"
            )
        )
        .toFile()
        .writeText("""
package com.capital7software;

/**
 * The Foo class.
 **/
public class Foo {
  /**
   * The FOO constant.
   **/
  final static public String FOO = "BAR";
  
  /**
   * The default constructor.
   **/
  public Foo() {
  }
  
  /**
   * The bar method.
   **/
  public void bar() {
  }
}
        """)

    val result = runTaskWithFailure("build")

    assertEquals(TaskOutcome.FAILED, result.task(":checkstyleMain")?.outcome)
    assertTrue(result.output.contains("Checkstyle rule violations were found."))
    assertTrue(result.output.contains("Checkstyle violations by severity: [warning:1]"))
  }

  @Test
  fun `fails on spotbugs error`() {
    Files
        .createDirectories(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software"
            )
        )
        .toFile()
    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software",
                "Foo.java"
            )
        )
        .toFile()
        .writeText("""
package com.capital7software;

/**
 * The Foo class.
 **/
public class Foo {
  /**
   * The default constructor.
   **/
  public Foo() {
  }
  
  /**
   * The bar method.
   **/
  public void bar() {
    String s = null;
    s.hashCode();
  }
}
        """)

    val result = runTaskWithFailure("build")

    assertEquals(TaskOutcome.FAILED, result.task(":spotbugsMain")?.outcome)
  }

  @Test
  fun `warns on deprecated API usage`() {
    Files
        .createDirectories(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software"
            )
        )
        .toFile()
    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software",
                "Foo.java"
            )
        )
        .toFile()
        .writeText("""
package com.capital7software;

/**
 * The Foo class.
 **/
public class Foo {
  /**
   * The default constructor.
   **/
  public Foo() {
  }
  
  /**
   * The deprecatedMethod method.
   **/
  @Deprecated
  public void deprecatedMethod() {}
}
        """)

    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "src",
                "main",
                "java",
                "com",
                "capital7software",
                "Bar.java"
            )
        )
        .toFile()
        .writeText("""
package com.capital7software;

/**
 * The Bar class.
 **/
public class Bar {
  /**
   * The default constructor.
   **/
  public Bar() {
  }
  
  /**
   * The bar method.
   **/
  public void bar() {
    new Foo().deprecatedMethod();
  }
}
        """)

    val result = runTask("build")

    assertEquals(TaskOutcome.SUCCESS, result.task(":build")?.outcome)
    assertTrue(result.output.contains("warning: [deprecation] deprecatedMethod()"))
  }
}
