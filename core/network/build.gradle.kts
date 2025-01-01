plugins {
    id("piece.android.library")
    id("piece.android.hilt")
}

android {
    namespace = "com.puzzle.network"

    buildTypes {
        debug {
            buildConfigField(
                "String",
                "PIECE_BASE_URL",
                "\"${properties["PIECE_DEV_BASE_URL"]}\"",
            )
        }
        release {
            buildConfigField(
                "String",
                "PIECE_BASE_URL",
                "\"${properties["PIECE_PROD_BASE_URL"]}\"",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
}
