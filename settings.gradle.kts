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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Piece"
include(":app")
include(":feature:auth")
include(":feature:mypage")
include(":feature:matching")
include(":feature:etc")
include(":core:domain")
include(":core:designsystem")
include(":core:data")
include(":core:network")
include(":core:navigation")
