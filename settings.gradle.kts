rootProject.name = "aoc"
plugins {
    id("com.mooltiverse.oss.nyx") version "2.5.1"
}

include("aoc-library", "aoc-2015", "aoc-2023")

configure<com.mooltiverse.oss.nyx.gradle.NyxExtension> {
    preset = "extended"

    changelog {
        path = "build/CHANGELOG.md"
    }
    releaseTypes {
        publicationServices.set(listOf("github"))
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
