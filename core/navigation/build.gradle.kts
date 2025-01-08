plugins {
    id("piece.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.puzzle.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
