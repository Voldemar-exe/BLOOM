pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "BLOOM"
include(":app")
include(":core:database")
include(":core:network")
include(":core:data")
include(":feature:auth")
include(":feature:sync")
include(":feature:notification")
include(":feature:profile")
include(":feature:gamification")
include(":feature:habit")
include(":feature:task")
include(":feature:plant")
include(":feature:stats")
include(":core:navigation")
