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
include(":core:datastore")
include(":core:network")
include(":core:data")
include(":core:model")
include(":core:navigation")
include(":core:designsystem")
include(":core:plant")
include(":core:gamification")
include(":core:notification")
include(":core:sync")

include(":feature:auth")
include(":feature:profile")
include(":feature:habit")
include(":feature:task")
include(":feature:stats")
include(":core:ui")
