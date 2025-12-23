rootProject.name = "aoc"

pluginManagement {
    includeBuild("build-release-settings")
}

plugins {
  id("com.mooltiverse.oss.nyx") version "3.1.4"
  id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
  id("com.capital7software.release-settings")
}

include("aoc-library")
include("aoc-2015")
include("aoc-2016")
include("aoc-2017")
include("aoc-2018")
include("aoc-2023")
include("aoc-2024")

configure<com.mooltiverse.oss.nyx.gradle.NyxExtension> {
  preset = "extended"

  initialVersion = "1.0.0"

  changelog {
    path = "CHANGELOG.md"
    append = "head"
    sections = mapOf(
        Pair("Added", "^(feat|:boom:|:sparkles:|:pushpin:|:heavy_plus_sign:)$"),
        Pair("Changed", "^(feat|:boom:|:arrow_up:|:lipstick:|:wrench:|:pencil2:)$"),
        Pair("Fixed", "^(fix|:bug:|:ambulance:)$"),
        Pair("Removed", "^(:fire:|:zap:|:arrow_down:|:heavy_minus_sign:)$"),
        Pair("Security", "^:lock:$"),
    )
  }

  commitMessageConventions {
    enabled.set(
        mutableListOf(
          "conventionalCommits",
          "gitmoji",
          "conventionalCommitsForMerge"
      )
    )
  }

  releaseTypes {
    enabled = listOf("release, ")
    publicationServices.set(listOf("github"))
  }

  services.create("github") {
    type = "GITHUB"
    options.apply {
      // The authentication token is read from the GH_TOKEN environment variable.
      put("AUTHENTICATION_TOKEN", "{{#environmentVariable}}GITHUB_TOKEN{{/environmentVariable}}")
      put("REPOSITORY_NAME", "advent-of-code")
      put("REPOSITORY_OWNER", "vjpalodichuk")
    }
  }

  resume = false
  summary = true
  summaryFile = ".nyx-summary.txt"
  stateFile = ".nyx-state.yml"
}
