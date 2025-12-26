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
        ?: error("Missing required property: $name or env $env")

val artifactoryContextUrl: String =
    requiredProperty("artifactoryContextUrl", "ARTIFACTORY_CONTEXT_URL")

val artifactoryRepoKeyPublish: String =
    requiredProperty("artifactoryRepoKeyPublishSnapshot", "ARTIFACTORY_REPO_KEY_PUBLISH")

val artifactoryRepoKeyPublishRelease: String =
    requiredProperty("artifactoryRepoKeyPublishRelease", "ARTIFACTORY_REPO_KEY_PUBLISH_RELEASE")

val artifactoryRepoKeyPublishSnapshot: String =
    requiredProperty("artifactoryRepoKeyPublishSnapshot", "ARTIFACTORY_REPO_KEY_PUBLISH_SNAPSHOT")

val artifactoryUser: String =
    requiredProperty("artifactoryUser","ARTIFACTORY_USER")

val artifactoryToken: String =
    requiredProperty("artifactoryToken","ARTIFACTORY_TOKEN")

val javadocJar by tasks.named<Jar>("dokkaJavadocJar")
val htmlJar by tasks.named<Jar>("dokkaHtmlJar")

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
      url = uri("${artifactoryContextUrl}/${artifactoryRepoKeyPublish}/")
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
      repoKey = artifactoryRepoKeyPublish
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

rootProject.tasks.named("nyxClean").let { clean ->
  tasks.named("clean") {
    dependsOn(clean)
  }
}
