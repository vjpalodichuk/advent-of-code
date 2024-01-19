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
val repoKeyValue: String by project
val repoKeyValuePublish: String by project
val artifactoryUser: String by project
val artifactoryPassword: String by project

// Projects should use Maven Central for external dependencies
// This could be the organization's private repository
repositories {
    maven {
        name = "artifactory-publish"
        url = uri("${artifactoryContextUrl}/${repoKeyValue}/")
        credentials {
            username = artifactoryUser
            password = artifactoryPassword
        }
    }
    mavenCentral()
}

// Use the Checkstyle rules provided by the convention plugin
// Do not allow any warnings
checkstyle {
    config = resources.text.fromString(com.capital7software.CheckstyleUtil.getCheckstyleConfig("/checkstyle.xml"))
    maxWarnings = 0
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    implementation("com.github.spotbugs:spotbugs-annotations:${spotbugs.toolVersion.get()}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

// Enable deprecation messages when compiling Java code
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
    exclude("**/module-info.java")
}