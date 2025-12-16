package com.capital7software.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class ReleaseSettingsPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {

        settings.gradle.beforeProject {
          if (project.rootProject == project) {
            project.pluginManager.apply("com.capital7software.release-root")
          }
        }
    }
}