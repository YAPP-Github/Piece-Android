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
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

rootProject.name = "Piece"
include(":app")

include(":core:domain")
include(":core:designsystem")
include(":core:data")
include(":core:network")
include(":core:navigation")
include(":core:common-ui")
include(":core:database")
include(":core:datastore")
include(":core:common")

include(":feature:auth")
include(":feature:profile")
include(":feature:matching")
include(":feature:setting")
include(":feature:onboarding")
include(":presentation")
