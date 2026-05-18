plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

object Versions {
  const val JETBRAINS_ANNOTATIONS = "26.0.2"
  const val JUNIT = "5.11.4"
  const val ARTIFACTORY = "5+"
  const val SPOTBUGS = "6.4.8"
  const val KOTLIN = "2.3.0"
  const val DETEKT = "1.23.8"
  const val DOKKA = "2.1.0"
}

dependencies {
  implementation("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
  implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:${Versions.SPOTBUGS}")
  implementation("org.jfrog.buildinfo:build-info-extractor-gradle:${Versions.ARTIFACTORY}")
  implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${Versions.KOTLIN}")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT}")
  implementation("org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}")
  // Test dependencies
  testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

configurations.all {
  this.exclude(group = "ch.qos.logback")
}

configurations.configureEach {
  resolutionStrategy.eachDependency {

    when ("${requested.group}:${requested.name}") {

      // ---------------------------------------------------------------------
      // Apache Commons 3
      // >= 3.0 && < 3.20.0 -> 3.20.0
      // ---------------------------------------------------------------------
      "org.apache.commons:commons-lang3" -> {
        requested.version?.let { version ->
          if (
            version >= "3.0" && version < "3.20,0"
          ) {
            useVersion("3.20.0")
            because("https://github.com/vjpalodichuk/advent-of-code/security/dependabot/12")
          }
        }
      }
      // ---------------------------------------------------------------------
      // Bouncy Castle
      // >= 1.71 && < 1.84 -> 1.84
      // ---------------------------------------------------------------------
      "org.bouncycastle:bcprov-jdk18on" -> {
        requested.version?.let { version ->
          if (
            version >= "1.71" &&
            version < "1.84"
          ) {
            useVersion("1.84")
            because("https://github.com/vjpalodichuk/advent-of-code/security/" +
                        "dependabot/23, https://github.com/vjpalodichuk/advent-of-code/" +
                        "security/dependabot/24")
          }
        }
      }

      // ---------------------------------------------------------------------
      // Plexus Utils
      // < 3.6.1 -> 3.6.1
      // ---------------------------------------------------------------------
      "org.codehaus.plexus:plexus-utils" -> {
        requested.version?.let { version ->
          if (version < "3.6.1") {
            useVersion("3.6.1")
            because("https://github.com/vjpalodichuk/advent-of-code/security/" +
                        "dependabot/19")
          }
        }
      }

      // ---------------------------------------------------------------------
      // Log4j Core
      // >= 2.0-alpha1 && < 2.25.4 -> 2.25.4
      // ---------------------------------------------------------------------
      "org.apache.logging.log4j:log4j-core" -> {
        requested.version?.let { version ->
          if (
            version >= "2.0-alpha1" && version < "2.25.4"
          ) {
            useVersion("2.25.4")
            because("https://github.com/vjpalodichuk/advent-of-code/security/" +
                        "dependabot/15, https://github.com/vjpalodichuk/advent-of-code/" +
                        "security/dependabot/20, https://github.com/vjpalodichuk/" +
                        "advent-of-code/security/dependabot/21, https://github.com/" +
                        "vjpalodichuk/advent-of-code/security/dependabot/22")
          }
        }
      }

      // ---------------------------------------------------------------------
      // Jackson Core
      // >= 2.19.0 && < 2.21.1 -> 2.21.1
      // >= 2.0.0 && <= 2.18.5 -> 2.18.6
      // ---------------------------------------------------------------------
      "com.fasterxml.jackson.core:jackson-core" -> {
        requested.version?.let { version ->

          when {
            version >= "2.19.0" &&
              version < "2.21.1" -> {

              useVersion("2.21.1")
              because("https://github.com/vjpalodichuk/advent-of-code/security/" +
                          "dependabot/17")
            }

            version >= "2.0.0" && version <= "2.18.5" -> {

              useVersion("2.18.6")
              because("https://github.com/vjpalodichuk/advent-of-code/security/" +
                          "dependabot/16")
            }

            else -> {
              useVersion(version)
            }
          }
        }
      }
    }
  }
}
