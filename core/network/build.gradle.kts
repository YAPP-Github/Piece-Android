import java.util.Properties

plugins {
    id("piece.android.library")
    id("piece.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.puzzle.network"

    val localProperties = Properties()
    localProperties.load(project.rootProject.file("local.properties").bufferedReader())

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "PIECE_BASE_URL",
                "\"${localProperties["PIECE_DEV_BASE_URL"]}\"",
            )
        }
        release {
            buildConfigField(
                "String",
                "PIECE_BASE_URL",
                "\"${localProperties["PIECE_PROD_BASE_URL"]}\"",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}
