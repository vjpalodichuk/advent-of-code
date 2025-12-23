package com.capital7software.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.api.tasks.Exec

@Suppress("unused")
class ReleaseRootPlugin : Plugin<Project> {

    override fun apply(project: Project) {
      if (project != project.rootProject) return

      val nyxValidateTask = project.tasks.register<Exec>("nyxValidate") {
        group = "release"
        description = "Validates Nyx release preconditions"

        val isSnapshot = project.version.toString().split('.', '-').size != 3

        commandLine("bash", "-c", """
          set -euo pipefail
          
          echo "SNAPSHOT: $isSnapshot"
      
          [[ "${'$'}{CI:-}" == "true" ]] || { echo "CI must be set to true"; exit 1; }
      
          ./gradlew -q nyxInfer
        """.trimIndent())
      }

      project.tasks.register<Exec>("nyxRelease") {
        group = "release"
        description = "Runs the Nyx-based release pipeline (root project only)"

        dependsOn(nyxValidateTask)

        commandLine(
            "bash", "-c", """
            set -euo pipefail

            ./gradlew -q nyxMark

            VERSION=${'$'}(./gradlew -q nyxInfer | tail -n1 | grep '^Project version:' | awk '{print ${'$'}3}')

            if [[ -n "${'$'}(git status --porcelain CHANGELOG.md)" ]]; then
              git add CHANGELOG.md
              git commit -S -m "chore(release): v${'S'}{VERSION}"
            fi

            [[ "${'$'}{NYX_RELEASE_APPROVED:-}" == "true" ]] || exit 1

            ./gradlew release publish
            """.trimIndent()
        )
      }

    }
}
