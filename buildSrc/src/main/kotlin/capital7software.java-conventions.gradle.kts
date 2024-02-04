import com.github.spotbugs.snom.SpotBugsTask

// Define Java conventions for this organization.
// Projects need to use the Java, Checkstyle and Spotbugs plugins.

plugins {
  java
  checkstyle
  kotlin("jvm")
  // NOTE: external plugin version is specified in implementation dependency artifact of the project's build file
  id("com.github.spotbugs")
  id("com.jfrog.artifactory")
  id("io.gitlab.arturbosch.detekt")
}

// Java Projects need to use the latest version!
java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
  withJavadocJar()
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }

  compilerOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
  }
}

val artifactoryContextUrl: String by project
val artifactoryRepoKeyReadRelease: String by project
val artifactoryRepoKeyPublishRelease: String by project
val artifactoryUser: String by project
val artifactoryPassword: String by project

object Versions {
  const val JETBRAINS_ANNOTATIONS = "24.0.1"
  const val JUNIT = "5.9.2"
  const val LOG4J2 = "2.22.1"
  const val JACKSON = "2.16.1"
  const val LOMBOK = "1.18.30"
  const val CHECKSTYLE = "10.13.0"
  const val DETEKT = "1.23.5"
  const val KOTLIN = "1.9.22"
}

// Projects should use Maven Central for external dependencies
// This could be the organization's private repository
repositories {
  maven {
    name = "artifactory-publish"
    url = uri("${artifactoryContextUrl}/${artifactoryRepoKeyReadRelease}/")
    credentials {
      username = artifactoryUser
      password = artifactoryPassword
    }
  }
  mavenCentral()
}

detekt {
  toolVersion = Versions.DETEKT

  config.setFrom(files(project.rootDir.resolve("conf/detekt.yml")))
  buildUponDefaultConfig = true

}

// Use the Checkstyle rules provided by the convention plugin
// Do not allow any warnings
checkstyle {
  config = resources.text
      .fromString(com.capital7software.CheckstyleUtil.getCheckstyleConfig("/checkstyle.xml"))
  maxWarnings = 0
  toolVersion = Versions.CHECKSTYLE
}

dependencies {
  implementation("org.jetbrains:annotations:${Versions.JETBRAINS_ANNOTATIONS}")
  implementation("com.github.spotbugs:spotbugs-annotations:${spotbugs.toolVersion.get()}")
  implementation("com.fasterxml.jackson.core:jackson-core:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.core:jackson-annotations:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.module:jackson-modules-java8:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-properties:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.JACKSON}")
  implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Versions.JACKSON}")
  implementation("org.apache.logging.log4j:log4j-api:${Versions.LOG4J2}")
  implementation("org.apache.logging.log4j:log4j-core:${Versions.LOG4J2}")
  implementation("org.apache.logging.log4j:log4j-slf4j2-impl:${Versions.LOG4J2}")
  compileOnly("org.projectlombok:lombok:${Versions.LOMBOK}")
  annotationProcessor("org.projectlombok:lombok:${Versions.LOMBOK}")
  // Test dependencies
  testCompileOnly("org.projectlombok:lombok:${Versions.LOMBOK}")
  testAnnotationProcessor("org.projectlombok:lombok:${Versions.LOMBOK}")
  testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
  useJUnitPlatform()
}

// Enable deprecation and unchecked messages when compiling Java code
tasks.withType<JavaCompile>().configureEach {
  options.compilerArgs.add("-Xlint:deprecation")
  options.compilerArgs.add("-Xlint:unchecked")
}

tasks.withType<SpotBugsTask>().configureEach {
  reports.create("html") {
    required = true
    outputLocation = file("${project.layout.buildDirectory.get()}/reports/spotbugs.html")
    setStylesheet("fancy-hist.xsl")
  }
}

tasks.withType<Checkstyle>().configureEach {
  reports {
    xml.required.set(true)
    html.required.set(true)
  }
}

// Resolve google collections and guava conflict
configurations.checkstyle {
  resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
    select("com.google.guava:guava:0")
  }
}