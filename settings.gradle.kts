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
    path = "build/CHANGELOG.md"
    append = "" // Recreate the changelog
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
    publicationServices.set(listOf("github"))
    enabled = listOf("mainline", "release", "feature", "fix", "integration", "snapshot")

    items.create("mainline") {
      collapseVersions = false
      description = """{{#fileContent}}build/CHANGELOG.md{{/fileContent}}"""
      filterTags = """^({{configuration.releasePrefix}})?([0-9]\d*)\.([0-9]\d*)\.([0-9]\d*)$"""
      gitCommit = "false"
      gitCommitMessage = """Release v{{version}}"""
      gitPush = "true"
      gitTag = "true"
      gitTagMessage = """Release v{{version}}"""
      matchBranches = """^(master|main)$"""
      matchWorkspaceStatus = "CLEAN"
      publish = "true"
      publishDraft = "false"
      publishPreRelease = "false"
      versionRangeFromBranchName = false
    }

    items.create("release") {
      collapseVersions = true
      collapsedVersionQualifier = """{{#sanitizeLower}}{{branch}}{{/sanitizeLower}}"""
      description = """{{#fileContent}}build/CHANGELOG.md{{/fileContent}}"""
      filterTags = """^({{configuration.releasePrefix}})?([0-9]\d*)\.([0-9]\d*)\.([0-9]\d*)(-(rel|release)((\.([0-9]\d*))?)?)$"""
      gitCommit = "false"
      gitCommitMessage = "Release version {{version}}"
      gitPush = "true"
      gitTag = "true"
      gitTagMessage = "Release v{{version}}"
      matchBranches = """^(rel|release)(-|\/)({{configuration.releasePrefix}})?([0-9|x]\d*)(\.([0-9|x]\d*)(\.([0-9|x]\d*))?)?$"""
      matchWorkspaceStatus = "CLEAN"
      publish = "true"
      publishDraft = "true"
      publishPreRelease = "false"
      versionRangeFromBranchName = true
    }

    items.create("feature") {
      collapseVersions = true
      collapsedVersionQualifier = """{{#sanitizeLower}}{{branch}}{{/sanitizeLower}}"""
      filterTags = """^({{configuration.releasePrefix}})?([0-9]\d*)\.([0-9]\d*)\.([0-9]\d*)(-(feat|feature)(([0-9a-zA-Z]*)(\.([0-9]\d*))?)?)$"""
      gitCommit = "false"
      gitPush = "false"
      gitTag = "false"
      matchBranches = """^(feat|feature)((-|\\/)[0-9a-zA-Z-_]+)?$"""
      matchWorkspaceStatus = "CLEAN"
      publish = "false"
      publishDraft = "false"
      publishPreRelease = "false"
      versionRangeFromBranchName = false
    }

    items.create("fix") {
      collapseVersions = true
      collapsedVersionQualifier = """{{#sanitizeLower}}{{branch}}{{/sanitizeLower}}"""
      filterTags = """^({{configuration.releasePrefix}})?([0-9]\d*)\.([0-9]\d*)\.([0-9]\d*)(-fix(([0-9a-zA-Z]*)(\.([0-9]\d*))?)?)$"""
      gitCommit = "false"
      gitPush = "false"
      gitTag = "false"
      matchBranches = """^fix((-|\/)[0-9a-zA-Z-_]+)?$"""
      publish = "false"
      publishDraft = "false"
      publishPreRelease = "false"
      versionRangeFromBranchName = false
    }

    items.create("integration") {
      collapseVersions = true
      collapsedVersionQualifier = """{{#sanitizeLower}}{{branch}}{{/sanitizeLower}}"""
      description = """{{#fileContent}}build/CHANGELOG.md{{/fileContent}}"""
      filterTags = """^({{configuration.releasePrefix}})?([0-9]\d*)\.([0-9]\d*)\.([0-9]\d*)(-(develop|development|integration|latest)(\.([0-9]\d*))?)$"""
      gitCommit = "false"
      gitPush = "true"
      gitTag = "true"
      matchBranches = """^(develop|development|integration|latest)$"""
      matchWorkspaceStatus = "CLEAN"
      identifiers.create("0") {
        position = "BUILD"
        qualifier = "s"
        value = "SNAPSHOT"
      }
      publish = "true"
      publishDraft = "false"
      publishPreRelease = "false"
      releaseLenient = true
      versionRangeFromBranchName = false
    }

    items.create("snapshot") {
      collapseVersions = true
      collapsedVersionQualifier = """{{#sanitizeLower}}{{branch}}{{/sanitizeLower}}"""
      gitCommit = "false"
      gitPush = "false"
      gitTag = "false"
      identifiers.create("0") {
        position = "BUILD"
        qualifier = "t"
        value = """{{#timestampYYYYMMDDHHMMSS}}{{timestamp}}{{/timestampYYYYMMDDHHMMSS}}"""
      }
      identifiers.create("1") {
        position = "BUILD"
        qualifier = "s"
        value = "SNAPSHOT"
      }
      publish = "false"
      publishDraft = "false"
      publishPreRelease = "false"
      versionRangeFromBranchName = false
    }
  }

  services.create("github") {
    type = "GITHUB"
    options.apply {
      // The authentication token is read from the GITHUB_TOKEN environment variable.
      put("AUTHENTICATION_TOKEN", "{{#environmentVariable}}GITHUB_TOKEN{{/environmentVariable}}")
      put("REPOSITORY_NAME", "advent-of-code")
      put("REPOSITORY_OWNER", "vjpalodichuk")
    }
  }

  resume = true
  summary = true
  summaryFile = "build/.nyx-summary.txt"
  stateFile = "build/.nyx-state.yml"

  dryRun = System.getenv("NYX_DRY_RUN") == "true"
}
