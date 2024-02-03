plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

object Versions {
  const val JETBRAINS_ANNOTATIONS = "24.0.1"
  const val JUNIT = "5.9.2"
  const val ARTIFACTORY = "5+"
  const val SPOTBUGS = "6.0.3"
  const val KOTLIN = "1.9.22"
  const val DETEKT = "1.23.5"
}

dependencies {
  implementation("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
  implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:${Versions.SPOTBUGS}")
  implementation("org.jfrog.buildinfo:build-info-extractor-gradle:${Versions.ARTIFACTORY}")
  implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:${Versions.KOTLIN}")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT}")

  // Test dependencies
  testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
