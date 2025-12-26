package com.capital7software.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import com.mooltiverse.oss.nyx.state.State
import org.gradle.api.file.RegularFile
import org.gradle.api.logging.Logger

@Suppress("unused")
class NyxReleasePlugin : Plugin<Project> {

  override fun apply(project: Project) {
    if (project != project.rootProject) return

    val infer = project.tasks.named("nyxInfer")
    val make = project.tasks.named("nyxMake")
    val mark = project.tasks.named("nyxMark")
    val publish = project.tasks.named("nyxPublish")

    val nyxState = project.objects.property(State::class.java)
    val dryRun = System.getenv("NYX_DRY_RUN") == "true"
    val nyxApproved = System.getenv("NYX_RELEASE_APPROVED") == "true" || dryRun
    val inferGuarded = project.tasks.register("nyxInferGuarded") {
      group = "release"
      description = "Runs nyxInfer and loads the shared Nyx state"

      dependsOn(infer)

      doLast {

        project.logger.lifecycle(
            "nyxInferGuarded starting (dryRun=$dryRun)"
        )

        val initialState = project.extra.get("nyxState") as? State?

        if (initialState == null) {
          project.logger.lifecycle("Nyx state not found. Aborting!")
          return@doLast
        }

        nyxState.set(initialState)
      }
    }

    val logNyxState = project.tasks.register("nyxState") {
      group = "release"
      description = "Runs nyxInferGuarded and logs the shared Nyx state"

      dependsOn(inferGuarded)

      doLast {
        logState(project.logger, nyxState.get())

        val outPath = System.getenv("GITHUB_OUTPUT")
        outPath?.let {
          project.logger.lifecycle("Writing state to $outPath")
          val outputFile = project.layout.projectDirectory.file(outPath)

          logState(outputFile, nyxState.get())
        } ?: project.logger.lifecycle("No GITHUB_OUTPUT set")
      }

    }

    val nyxMakeGuarded = project.tasks.register("nyxMakeGuarded") {
      group = "release"
      description = "Runs nyxState and nyxMake if a new version or release exists"

      dependsOn(logNyxState, make)

      onlyIf {
        nyxState.orNull?.let {
          (it.newRelease || it.newVersion) && !dryRun
        } ?: false
      }

      doFirst {
        project.logger.lifecycle("Executing nyxState and nyxMake")
      }
    }

    val nyxMarkGuarded = project.tasks.register("nyxMarkGuarded") {
      group = "release"
      description = "Runs nyxMakeGuarded and nyxMark if a new version or release exists. Tags the repo with the new version"

      dependsOn(nyxMakeGuarded, mark)

      onlyIf {
        nyxState.orNull?.let {
           (it.newRelease || it.newVersion) && !dryRun
        } ?: false
      }

      doFirst {
        project.logger.lifecycle("Executing nyxMakeGuarded and nyxMark")
      }
    }

    val nyxPublishGuarded = project.tasks.register("nyxPublishGuarded") {
      group = "release"
      description = "Runs nyxMarkGuarded and nyxPublish if a new release exists. Pushes tags and publishes the release to GitHub"

      dependsOn(nyxMarkGuarded, publish)

      onlyIf {
        nyxState.orNull?.let {
           (it.newRelease || it.newVersion) && !dryRun
        } ?: false
      }

      doFirst {
        project.logger.lifecycle("Executing nyxMarkGuarded and nyxPublish")
      }
    }

    project.tasks.register("nyxReleaseOrchestrated") {
      group = "release"
      description = "Fully orchestrated Nyx release!"

      dependsOn(nyxPublishGuarded)

      doFirst {
        require(nyxApproved) {
          "Release blocked: NYX_RELEASE_APPROVED != true"
        }

        project.logger.lifecycle(
            "Nyx orchestrated release starting (dryRun=$dryRun)"
        )

      }
    }
  }

  private fun logState(logger: Logger, state: State) {
    logger.lifecycle("branch=${state.branch}")
    logger.lifecycle("releaseType.gitTag=${state.releaseType.gitTag}")
    logger.lifecycle("releaseType.gitCommit=${state.releaseType.gitCommit}")
    logger.lifecycle("releaseType.gitPush=${state.releaseType.gitPush}")
    logger.lifecycle("releaseType.publish=${state.releaseType.publish}")
    logger.lifecycle("bump=${state.bump}")
    logger.lifecycle("directory=${state.directory.absolutePath}")
    logger.lifecycle("scheme=${state.scheme}")
    logger.lifecycle("timestamp=${state.timestamp}")
    logger.lifecycle("coreVersion=${state.coreVersion}")
    logger.lifecycle("latestVersion=${state.latestVersion}")
    logger.lifecycle("newVersion=${state.newVersion}")
    logger.lifecycle("newRelease=${state.newRelease}")
    logger.lifecycle("version=${state.version}")
    logger.lifecycle("versionMajorNumber=${state.versionMajorNumber}")
    logger.lifecycle("versionMinorNumber=${state.versionMinorNumber}")
    logger.lifecycle("versionPatchNumber=${state.versionPatchNumber}")
    logger.lifecycle("versionBuildMetadata=${state.versionBuildMetadata}")

    logger.lifecycle("significant_commits=${state.releaseScope.significantCommits.size}")
    state.releaseScope.significantCommits.forEachIndexed { index, commit ->
      logger.lifecycle("significant_commit_$index=\n{\n\tsha:\t\t${commit.sha}\n\tmessage:\t${commit.message.shortMessage}\n}\n")
    }

    logger.lifecycle("commits=${state.releaseScope.commits.size}")
    state.releaseScope.commits.forEachIndexed { index, commit ->
      logger.lifecycle("commit_$index=\n{\n\tsha:\t\t${commit.sha}\n\tmessage:\t${commit.message.shortMessage}\n}\n")
    }

  }

  private fun logState(file: RegularFile, state: State) {

    if (file.asFile.exists()) {
      file.asFile.writer().use { writer -> with(writer) {
        appendLine("nyx_branch=${state.branch}")
        appendLine("nyx_release_type_git_tag=${state.releaseType.gitTag}")
        appendLine("nyx_release_type_git_commit=${state.releaseType.gitCommit}")
        appendLine("nyx_release_type_git_push=${state.releaseType.gitPush}")
        appendLine("nyx_release_type_publish=${state.releaseType.publish}")
        appendLine("nyx_bump=${state.bump}")
        appendLine("nyx_directory=${state.directory.absolutePath}")
        appendLine("nyx_scheme=${state.scheme}")
        appendLine("nyx_timestamp=${state.timestamp}")
        appendLine("nyx_core_version=${state.coreVersion}")
        appendLine("nyx_latest_version=${state.latestVersion}")
        appendLine("nyx_new_version=${state.newVersion}")
        appendLine("nyx_new_release=${state.newRelease}")
        appendLine("nyx_version=${state.version}")
        appendLine("nyx_version_,ajor_number=${state.versionMajorNumber}")
        appendLine("nyx_version_minor_number=${state.versionMinorNumber}")
        appendLine("nyx_version_patch_number=${state.versionPatchNumber}")
        appendLine("nyx_version_build_metadata=${state.versionBuildMetadata}")

        appendLine("nyx_significant_commits=${state.releaseScope.significantCommits.size}")
        state.releaseScope.significantCommits.forEachIndexed { index, commit ->
          appendLine("nyx_significant_commit_sha_$index=${commit.sha}")
          appendLine("nyx_significant_commit_message_$index=${commit.message.shortMessage}")
        }

        appendLine("nyx_commits=${state.releaseScope.commits.size}")
        state.releaseScope.commits.forEachIndexed { index, commit ->
          appendLine("nyx_commit_sha_$index=${commit.sha}")
          appendLine("nyx_commit_message_$index=${commit.message.shortMessage}")
        }
      }}
    }
  }
}
