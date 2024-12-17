plugins {
    id("piece.android.library")
}

android {
    namespace = "com.puzzle.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
