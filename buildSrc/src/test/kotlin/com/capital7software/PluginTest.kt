package com.capital7software

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir

abstract class PluginTest {

  @TempDir
  protected lateinit var testProjectDir: Path
  protected lateinit var settingsFile: File
  protected lateinit var buildFile: File
  private lateinit var propertiesFile: File

  @BeforeEach
  fun setup() {
    settingsFile = Files
        .createFile(Paths.get(testProjectDir.toString(), "settings.gradle.kts"))
        .toFile()
    settingsFile.appendText("""
            rootProject.name = "test"
        """)
    buildFile = Files
        .createFile(Paths.get(testProjectDir.toString(), "build.gradle.kts"))
        .toFile()
    propertiesFile = Files
        .createFile(Paths.get(testProjectDir.toString(), "gradle.properties"))
        .toFile()
    propertiesFile.appendText("""
      artifactoryUser =
      artifactoryPassword =
      artifactoryContextUrl = https://artifactory.capital7software.com/artifactory
      artifactoryRepoKeyPublishRelease = libs-release-local
      artifactoryRepoKeyPublishSnapshot = libs-snapshot-local
      artifactoryRepoKeyReadRelease = libs-release
      artifactoryRepoKeyReadSnapshot = libs-snapshot
    """.trimIndent())
  }

  fun runTask(task: String): BuildResult {
    return GradleRunner.create()
        .withProjectDir(testProjectDir.toFile())
        .withArguments(task, "--stacktrace")
        .withPluginClasspath()
        .build()
  }

  fun runTaskWithFailure(task: String): BuildResult {
    return GradleRunner.create()
        .withProjectDir(testProjectDir.toFile())
        .withArguments(task, "--stacktrace")
        .withPluginClasspath()
        .buildAndFail()
  }

  fun getAvailableTasks(): BuildResult {
    return GradleRunner.create()
        .withProjectDir(testProjectDir.toFile())
        .withArguments("tasks", "--stacktrace")
        .withPluginClasspath()
        .build()
  }
}
