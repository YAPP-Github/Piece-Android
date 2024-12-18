plugins {
    id("piece.android.library")
    id("piece.android.hilt")
}

android {
    namespace = "com.puzzle.network"

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
