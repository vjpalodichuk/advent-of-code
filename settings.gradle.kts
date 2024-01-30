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

    releaseTypes {
        publicationServices.set(listOf("github"))
        remoteRepositories.set(listOf("origin"))
    }

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
