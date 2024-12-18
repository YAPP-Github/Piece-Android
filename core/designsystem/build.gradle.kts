plugins {
    id("piece.android.library")
    id("piece.android.compose")
}

android {
    namespace = "com.puzzle.designsystem"
}

dependencies {
    implementation(libs.lottie.compose)
}