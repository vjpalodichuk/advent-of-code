import com.github.spotbugs.snom.SpotBugsTask

// Define Java conventions for this organization.
// Projects need to use the Java, Checkstyle and Spotbugs plugins.

plugins {
    java
    checkstyle
    // NOTE: external plugin version is specified in implementation dependency artifact of the project's build file
    id("com.github.spotbugs")
}

// Java Projects need to use the latest version!
java {
    sourceCompatibility = JavaVersion.VERSION_21
}

// Projects should use Maven Central for external dependencies
// This could be the organization's private repository
repositories {
    maven {
        name = "artifactory-snapshots"
        url = uri("https://artifactory.capital7software.com/artifactory/libs-snapshot/")
    }
    mavenCentral()
}

// Use the Checkstyle rules provided by the convention plugin
// Do not allow any warnings
checkstyle {
    config = resources.text.fromString(com.capital7software.CheckstyleUtil.getCheckstyleConfig("/checkstyle.xml"))
    maxWarnings = 0
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