plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal() // so that external plugins can be resolved in dependencies section
}

object Versions {
  const val JETBRAINS_ANNOTATIONS = "24.0.1"
  const val JUNIT = "5.9.2"
  const val LOG4J2 = "2.22.1"
  const val JACKSON = "2.16.1"
  const val LOMBOK = "1.18.30"
  const val CHECKSTYLE = "10.13.0"
}

dependencies {
  implementation("org.jetbrains:annotations:24.0.1")
  implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.0.3")
  implementation("org.jfrog.buildinfo:build-info-extractor-gradle:5+")
  implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:1.9.22")
  // Test dependencies
  testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}
