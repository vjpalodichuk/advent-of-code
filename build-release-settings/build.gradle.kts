plugins {
    `kotlin-dsl`
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.mooltiverse.oss.nyx:main:3.1.4")
}

gradlePlugin {
    plugins {
        register("releaseSettings") {
            id = "com.capital7software.release-settings"
            implementationClass = "com.capital7software.gradle.plugin.ReleaseSettingsPlugin"
        }
        register("releaseRoot") {
            id = "com.capital7software.release-root"
            implementationClass = "com.capital7software.gradle.plugin.NyxReleasePlugin"
        }
    }
}

configurations.all {
  this.exclude(group = "ch.qos.logback")

  resolutionStrategy.dependencySubstitution {
    // Security Fixes!
    substitute(module("org.apache.commons:commons-lang3:3.1.4"))
        .using(module("org.apache.commons:commons-lang3:3.20.0"))
        .because("""https://github.com/vjpalodichuk/advent-of-code/security/dependabot/12""")
  }
}
