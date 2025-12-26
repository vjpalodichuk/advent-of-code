import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention

// Define Java Library conventions for this organization.
// Projects need to use the organization's Java conventions and publish using Maven Publish

plugins {
  `java-library`
  `maven-publish`
  id("com.jfrog.artifactory")
  id("com.capital7software.java-conventions")
}

// Projects have the 'com.capital7software.aoc' group by convention
group = "com.capital7software.aoc"

fun requiredProperty(name: String, env: String): String =
    System.getenv(env)
        ?: (findProperty(name) as String?)
        ?: "" //error("Missing required property: $name or env $env")

val artifactoryContextUrl: String by project
val artifactoryRepoKeyReadRelease: String by project
val artifactoryRepoKeyReadSnapshot: String by project
val artifactoryRepoKeyPublishRelease: String by project
val artifactoryRepoKeyPublishSnapshot: String by project

val artifactoryUser: String =
    requiredProperty("artifactoryUser","ARTIFACTORY_USER")

val artifactoryToken: String =
    requiredProperty("artifactoryToken","ARTIFACTORY_TOKEN")

val javadocJar by tasks.named<Jar>("dokkaJavadocJar")
val htmlJar by tasks.named<Jar>("dokkaHtmlJar")

val isSnapshot = version.toString().contains("SNAPSHOT")

configure<PublishingExtension> {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifact(javadocJar)
      artifact(htmlJar)
    }
  }

  repositories {
    maven {
      name = "artifactory"
      url = uri(
          if (isSnapshot)
            "${artifactoryContextUrl}/${artifactoryRepoKeyPublishSnapshot}/"
          else
            "${artifactoryContextUrl}/${artifactoryRepoKeyPublishRelease}/"
      )
      credentials {
        username = artifactoryUser
        password = artifactoryToken
      }
    }
  }
}

configure<ArtifactoryPluginConvention> {
  setContextUrl(artifactoryContextUrl)

  publish {
    contextUrl = artifactoryContextUrl
    repository {
      repoKey =
          if (isSnapshot) artifactoryRepoKeyPublishSnapshot else artifactoryRepoKeyPublishRelease
      releaseRepoKey = artifactoryRepoKeyPublishRelease
      snapshotRepoKey = artifactoryRepoKeyPublishSnapshot
      username = artifactoryUser
      password = artifactoryToken
    }
    defaults {
      setPublishPom(true)
      setPublishArtifacts(true)
      isPublishBuildInfo = true
      publications("ALL_PUBLICATIONS")
    }
  }

  clientConfig.info.apply {
    buildName = name
    buildNumber = version.toString()
    addEnvironmentProperty(
        "git.commit",
        System.getenv("GITHUB_SHA") ?: "local"
    )
  }
}

tasks.named("artifactoryPublish") {
  group = "publishing"

  doFirst {
    if (System.getenv("CI") != "true") {
        error("Build publishing may only run in CI")
    }
  }
}

gradle.taskGraph.whenReady {
  listOf(artifactoryUser, artifactoryToken).forEach { secret ->
    if (secret.isNotBlank()) {
      System.setProperty("org.gradle.logging.stacktrace", "none")
    }
  }
}
