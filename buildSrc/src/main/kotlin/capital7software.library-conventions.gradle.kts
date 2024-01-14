// Define Java Library conventions for this organization.
// Projects need to use the organization's Java conventions and publish using Maven Publish

plugins {
    `java-library`
    `maven-publish`
    id("capital7software.java-conventions")
}

// Projects have the 'com.capital7software' group by convention
group = "com.capital7software.aoc"

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "artifactory-publish"
            url = uri("https://artifactory.capital7software.com/artifactory/libs-snapshot-local/")
            credentials {
                username = "vincent"
                password = "cmVmdGtuOjAxOjE3MzY3NjQ1MTM6dW44NUxkNWVOaG9MWDh2YkRDRm9Kc0xkVVc3"
            }
        }
    }
}

// The project requires libraries to have a README containing sections configured below
val readmeCheck by tasks.registering(com.capital7software.ReadmeVerificationTask::class) {
    readme = layout.projectDirectory.file("README.md")
    readmePatterns = listOf("^## API$", "^## Changelog$")
}

tasks.named("check") { dependsOn(readmeCheck) }
