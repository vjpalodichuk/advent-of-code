import com.github.spotbugs.snom.SpotBugsTask

// Define Java conventions for this organization.
// Projects need to use the Java, Checkstyle and Spotbugs plugins.

plugins {
  java
  checkstyle
  kotlin("jvm")
  // NOTE: external plugin version is specified in implementation dependency
  // artifact of the project's build file
  id("com.github.spotbugs")
  id("com.jfrog.artifactory")
  id("io.gitlab.arturbosch.detekt")
  id("org.jetbrains.dokka")
  id("org.jetbrains.dokka-javadoc")
}

// Java Projects need to use the latest version!
java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }

  compilerOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
  }
}

fun requiredProperty(name: String, env: String): String =
    (findProperty(name) as String?)
        ?: System.getenv(env)
        ?: error("Missing required property: $name or env $env")

val artifactoryContextUrl: String =
    requiredProperty("artifactoryContextUrl", "ARTIFACTORY_CONTEXT_URL")

val artifactoryRepoKeyReadRelease: String =
    requiredProperty("artifactoryRepoKeyReadRelease", "ARTIFACTORY_REPO_KEY_READ_RELEASE")

val artifactoryRepoKeyReadSnapshot: String by project
    requiredProperty("artifactoryRepoKeyReadSnapshot", "ARTIFACTORY_REPO_KEY_READ_SNAPSHOT")

val artifactoryUser: String =
    requiredProperty("artifactoryUser", "ARTIFACTORY_USER")

val artifactoryToken: String =
    requiredProperty("artifactoryToken", "ARTIFACTORY_TOKEN")

object Versions {
  const val JETBRAINS_ANNOTATIONS = "26.0.2"
  const val JUNIT = "5.11.4"
  const val LOG4J2 = "2.25.3"
  const val JACKSON = "2.19.4"
  const val LOMBOK = "1.18.42"
  const val CHECKSTYLE = "12.3.0"
  const val DETEKT = "1.23.8"
  const val KOTLIN = "2.3.0"
}

// Projects should use Maven Central for external dependencies
// This could be the organization's private repository
repositories {
  maven {
    name = "artifactory-release"
    url = uri("${artifactoryContextUrl}/${artifactoryRepoKeyReadRelease}/")
    credentials {
      username = artifactoryUser
      password = artifactoryToken
    }
  }

  maven {
    name = "artifactory-snapshot"
    url = uri("${artifactoryContextUrl}/${artifactoryRepoKeyReadSnapshot}/")
    credentials {
      username = artifactoryUser
      password = artifactoryToken
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
      .fromString(com.capital7software.gradle.plugin.CheckstyleUtil.getCheckstyleConfig("/checkstyle.xml"))
  maxWarnings = 0
  toolVersion = Versions.CHECKSTYLE
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.KOTLIN}")
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
  testImplementation("org.jetbrains.kotlin:kotlin-test:${Versions.KOTLIN}")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:${Versions.KOTLIN}")
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

tasks.withType<Javadoc>().configureEach {
  isFailOnError = false
}

val htmlJar by tasks.register<Jar>("dokkaHtmlJar") {
  group = "build"
  dependsOn(tasks.dokkaGeneratePublicationHtml)
  from(tasks.dokkaGeneratePublicationHtml)
  archiveClassifier.set("html-docs")
}

val javadocJar by tasks.register<Jar>("dokkaJavadocJar") {
  group = "build"
  dependsOn(tasks.dokkaGeneratePublicationJavadoc)
  from(tasks.dokkaGeneratePublicationJavadoc)
  archiveClassifier.set("javadoc")
}

// The project requires libraries to have a README containing sections configured below
val readmeCheck by tasks.registering(com.capital7software.gradle.plugin.ReadmeVerificationTask::class) {
  readme = layout.projectDirectory.file("README.md")
  readmePatterns = listOf("^## API$")
}

tasks.named("check") { dependsOn(readmeCheck) }

// Resolve google collections and guava conflict
configurations.checkstyle {
  resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
    select("com.google.guava:guava:0")
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
