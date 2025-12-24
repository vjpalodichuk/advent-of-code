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

        val isSnapshot = project.version.toString().split('.', '-', '+').size != 3

        commandLine("bash", "-c", """
          set -euo pipefail
          
          echo "SNAPSHOT: $isSnapshot"
      
          [[ "${'$'}{CI:-}" == "true" ]] || { echo "CI must be set to true"; exit 1; }
      
          VERSION=$(./gradlew -q nyxInfer | tail -n1 | grep '^Project version:' | awk '{print $3}')
          
          echo "chore(release): v${'$'}{VERSION}"
        """.trimIndent())
      }

      project.tasks.register<Exec>("nyxRelease") {
        group = "release"
        description = "Runs Nyx and conditionally commits, tags, and pushes based on environment guards"

        dependsOn(nyxValidateTask)

        commandLine(
            "bash", "-c", """
            ########################################
            # 1. Global approval gate
            ########################################
            if [[ "${'$'}{NYX_RELEASE_APPROVED:-false}" != "true" ]]; then
              echo "NYX_RELEASE_APPROVED != true; skipping entire release process"
              exit 0
            fi
    
            ########################################
            # 2. Run Nyx
            ########################################
            echo "Running Nyx mark..."
            ./gradlew nyxMark
    
            VERSION=$(./gradlew -q nyxInfer | tail -n1)
            if [[ -z "${'$'}VERSION" ]]; then
              echo "ERROR: Unable to determine Nyx version"
              exit 1
            fi
            echo "Calculated version: ${'$'}VERSION"
    
            ########################################
            # 3. Check changelog modification
            ########################################
            if ! git status --porcelain CHANGELOG.md | grep -q .; then
              echo "CHANGELOG.md not modified; skipping commit, tag, and push"
              exit 0
            fi
    
            ########################################
            # 4. Normalize CHANGELOG.md
            ########################################
            echo "Normalizing CHANGELOG.md"
    
            awk '
              BEGIN { seen_header=0; blank=0 }
              {
                if ($0 == "# Changelog") {
                  if (seen_header) next
                  seen_header=1
                }
    
                if ($0 ~ /^[[:space:]]*$/) {
                  if (blank) next
                  blank=1
                  print ""
                  next
                }
    
                blank=0
                print
              }
            ' CHANGELOG.md > CHANGELOG.md.tmp
    
            mv CHANGELOG.md.tmp CHANGELOG.md
    
            if ! git status --porcelain CHANGELOG.md | grep -q .; then
              echo "CHANGELOG.md normalization resulted in no effective changes"
              exit 0
            fi
    
            ########################################
            # 5. Conditional commit
            ########################################
            DID_COMMIT=false
            if [[ "${'$'}{GIT_COMMITTING:-false}" == "true" ]]; then
              echo "Creating signed changelog commit"
              git add CHANGELOG.md
              git commit -S -m "Release v${'$'}VERSION"
              DID_COMMIT=true
            else
              echo "Skipping commit: GIT_COMMITTING != true"
            fi
    
            ########################################
            # 6. Conditional tag
            ########################################
            DID_TAG=false
            if [[ "${'$'}{GIT_TAGGING:-false}" == "true" ]]; then
              echo "Creating signed tag ${'$'}VERSION"
              git tag -s "${'$'}VERSION" -m "Release v${'$'}VERSION"
              DID_TAG=true
            else
              echo "Skipping tag: GIT_TAGGING != true"
            fi
    
            ########################################
            # 7. Conditional push
            ########################################
            if [[ "${'$'}DID_COMMIT" == "true" || "${'$'}DID_TAG" == "true" ]]; then
              if [[ "${'$'}{GIT_PUSHING:-false}" == "true" ]]; then
                echo "Pushing release artifacts"
    
                if [[ "${'$'}DID_COMMIT" == "true" ]]; then
                  git push origin HEAD
                fi
    
                if [[ "${'$'}DID_TAG" == "true" ]]; then
                  git push origin "${'$'}VERSION"
                fi
              else
                echo "Skipping push: GIT_PUSHING != true"
              fi
            else
              echo "Skipping push: no commit or tag was created"
            fi
    
            ./gradlew release
            """.trimIndent()
        )
      }

    }
}
