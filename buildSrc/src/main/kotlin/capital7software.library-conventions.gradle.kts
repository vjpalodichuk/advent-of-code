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
            url = uri("${artifactory_contextUrl}/${repoKeyValuePublish}/")
            credentials {
                username = "${artifactory_user}"
                password = "${artifactory_password}"
            }
        }
    }
}

artifactory {
    setContextUrl("${artifactory_contextUrl}")

    publish {
        setContextUrl("${artifactory_contextUrl}")
        repository {
            setRepoKey("${repoKeyValuePublish}")
            setUsername("${artifactory_user}")
            setPassword("${artifactory_password}")
            setMavenCompatible(true)
        }
        defaults {
            setPublishPom(true)
            setPublishArtifacts(true)
            publications("ALL_PUBLICATIONS")
            //publishConfigs(configurations.findByName("archives")?.isCanBeResolved == true)
        }
    }
    resolve {
        repository {
            setRepoKey("${repoKeyValue}")
            setUsername("${artifactory_user}")
            setPassword("${artifactory_password}")
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
