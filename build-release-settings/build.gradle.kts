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

  resolutionStrategy {
    eachDependency {
      // Apache Commons (all artifacts)
      if (requested.group == "org.apache.commons" &&
          requested.version == "3.1.4"
      ) {
        useVersion("3.20.0")
        because("https://github.com/vjpalodichuk/advent-of-code/security/dependabot/12")
      }

      // Log4j (all artifacts)
      if (requested.group == "org.apache.logging.log4j" &&
          requested.version == "2.25.2"
      ) {
        useVersion("2.25.3")
        because("https://github.com/vjpalodichuk/advent-of-code/security/dependabot/15")
      }
    }
  }
}
