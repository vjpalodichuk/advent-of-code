rootProject.name = "aoc"
plugins {
    id("com.mooltiverse.oss.nyx") version "2.5.1"
}

include("aoc-library", "aoc-2015", "aoc-2023")

configure<com.mooltiverse.oss.nyx.gradle.NyxExtension> {
    preset = "extended"

    initialVersion = "1.0.0"

    changelog {
        path = "CHANGELOG.md"
        append = "head"
    }

//    releaseTypes {
//        publicationServices.set(listOf("github"))
//
//        items.create("release") {
//            collapseVersions.set(true)
//            collapsedVersionQualifier.set("{{#firstLower}}{{branch}}{{/firstLower}}")
//            filterTags.set("^({{configuration.releasePrefix}})?([0-9]\\d*)\\.([0-9]\\d*)\\.([0-9]\\d*)(-(rel|release)((\\.([0-9]\\d*))?)?)\$")
//            gitPushForce.set("true")
//            gitPush.set("true")
//            gitTag.set("true")
//            gitTagForce.set("true")
//            gitTagMessage.set("Release: {{configuration.releasePrefix}}{{version}}")
//            matchBranches.set("^(rel|release)(-|\\/)({{configuration.releasePrefix}})?([0-9|x]\\d*)(\\.([0-9|x]\\d*)(\\.([0-9|x]\\d*))?)?\$")
//            matchWorkspaceStatus.set("CLEAN")
//            versionRangeFromBranchName.set(true)
//        }
//    }

    services.create("github") {
        type = "GITHUB"
        options.apply {
            // The authentication token is read from the GH_TOKEN environment variable.
            put("AUTHENTICATION_TOKEN", "{{#environmentVariable}}GH_TOKEN{{/environmentVariable}}")
            put("REPOSITORY_NAME", "advent-of-code")
            put("REPOSITORY_OWNER", "vjpalodichuk")
        }
    }
}
