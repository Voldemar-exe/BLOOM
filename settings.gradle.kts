pluginManagement {
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "BLOOM"
include(":app")
include(":core:common")
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
