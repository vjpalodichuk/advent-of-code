// Define Java Library conventions for this organization.
// Projects need to use the organization's Java conventions and publish using Maven Publish

plugins {
    `java-library`
    `maven-publish`
    id("capital7software.java-conventions")
}

// Projects have the 'com.capital7software' group by convention
group = "com.capital7software.aoc"

val artifactoryContextUrl: String by project
val repoKeyValue: String by project
val repoKeyValuePublish: String by project
val artifactoryUser: String by project
val artifactoryPassword: String by project

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "artifactory-publish"
            url = uri("${artifactoryContextUrl}/${repoKeyValuePublish}/")
            credentials {
                username = artifactoryUser
                password = artifactoryPassword
            }
        }
    }
}

artifactory {
    setContextUrl(artifactoryContextUrl)

    publish {
        setContextUrl(artifactoryContextUrl)
        repository {
            setRepoKey(repoKeyValuePublish)
            setUsername(artifactoryUser)
            setPassword(artifactoryPassword)
            setMavenCompatible(true)
        }
        defaults {
            setPublishPom(true)
            setPublishArtifacts(true)
            publications("ALL_PUBLICATIONS")
        }
    }
    resolve {
        repository {
            setRepoKey(repoKeyValue)
            setUsername(artifactoryUser)
            setPassword(artifactoryPassword)
            setMavenCompatible(true)
        }
    }
}

// The project requires libraries to have a README containing sections configured below
val readmeCheck by tasks.registering(com.capital7software.ReadmeVerificationTask::class) {
    readme = layout.projectDirectory.file("README.md")
    readmePatterns = listOf("^## API$", "^## Changelog$")
}

tasks.named("check") { dependsOn(readmeCheck) }
