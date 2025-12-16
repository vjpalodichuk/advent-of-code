plugins {
    `kotlin-dsl`
}

repositories {
  mavenCentral()
}

gradlePlugin {
    plugins {
        register("releaseSettings") {
            id = "com.capital7software.release-settings"
            implementationClass = "com.capital7software.gradle.plugin.ReleaseSettingsPlugin"
        }
        register("releaseRoot") {
            id = "com.capital7software.release-root"
            implementationClass = "com.capital7software.gradle.plugin.ReleaseRootPlugin"
        }
    }
}
