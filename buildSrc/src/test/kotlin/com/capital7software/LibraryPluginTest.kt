package com.capital7software

import java.nio.file.Files
import java.nio.file.Paths
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LibraryPluginTest : PluginTest() {

  @BeforeEach
  fun init() {
    buildFile.appendText("""
            plugins {
                id("capital7software.library-conventions")
            }
        """)
  }

  @Test
  fun `can declare api dependencies`() {
    readmeContainingMandatorySectionsExists()
    buildFile.appendText("""
            dependencies {
                api("org.apache.commons:commons-lang3:3.4")
            }
        """)

    val result = runTask("build")

    assertEquals(TaskOutcome.SUCCESS, result.task(":build")?.outcome)
  }

  @Test
  fun `publishes library with versioning`() {
    readmeContainingMandatorySectionsExists()
    settingsFile.writeText("""
            rootProject.name = "my-library"
        """.trimIndent())
    buildFile.appendText("""
            version = "0.1.0"

            publishing {
                repositories {
                    maven {
                        name = "testRepo"
                        url = uri("build/test-repo")
                    }
                }
            }
        """)
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
                "Util.java"
            )
        )
        .toFile()
        .writeText("""
package com.capital7software;

/**
 * The Util class.
 **/
public class Util {
  /**
   * The default constructor.
   **/
  public Util() {
  }
  
  /**
   * The someUtil method.
   **/
  public static void someUtil() {
  }
}
        """)

    val result = runTask("publishMavenJavaPublicationToTestRepoRepository")

    System.out.println(result.output)
    System.out.println(result.tasks)

    assertEquals(TaskOutcome.SUCCESS, result.task(":jar")?.outcome)
    assertEquals(
        TaskOutcome.SUCCESS,
        result.task(":publishMavenJavaPublicationToTestRepoRepository")?.outcome
    )
    Files.list(Paths.get(
        testProjectDir.toString(),
        "build/test-repo/com/capital7software/aoc/my-library"
    )).forEach { System.out.println(it.toString()) }

    assertTrue(
        Files.exists(Paths.get(
            testProjectDir.toString(),
            "build/test-repo/com/capital7software/aoc/my-library/0.1.0/my-library-0.1.0.jar"
        ))
    )
  }

  @Test
  fun `fails when no README exists`() {
    val result = runTaskWithFailure("check")

    assertEquals(TaskOutcome.FAILED, result.task(":readmeCheck")?.outcome)
  }

  @Test
  fun `fails when README does not have API section`() {
    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "README.md"
            )
        )
        .toFile()
        .writeText("""
            ## Changelog
            - change 1
            - change 2
        """.trimIndent())

    val result = runTaskWithFailure("check")

    assertEquals(TaskOutcome.FAILED, result.task(":readmeCheck")?.outcome)
    assertTrue(result.output.contains("README should contain section: ^## API$"))
  }

  @Test
  fun `fails when README does not have Changelog section`() {
    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "README.md"
            )
        )
        .toFile()
        .writeText("""
            ## API
            public API description
        """.trimIndent())

    val result = runTaskWithFailure("check")

    System.out.println(result.output)
    assertEquals(TaskOutcome.FAILED, result.task(":readmeCheck")?.outcome, testProjectDir.toString())
    assertTrue(result.output.contains("README should contain section: ^## Changelog$"))
  }

  private fun readmeContainingMandatorySectionsExists() {
    Files
        .createFile(
            Paths.get(
                testProjectDir.toString(),
                "README.md"
            )
        )
        .toFile()
        .writeText("""
            ## API
            public API description

            ## Changelog
            - change 1
            - change 2
        """.trimIndent())
  }
}
