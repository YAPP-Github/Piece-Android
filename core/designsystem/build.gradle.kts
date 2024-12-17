plugins {
    id("piece.android.library")
    id("piece.android.compose")
}

android {
    namespace = "com.puzzle.designsystem"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.lottie.compose)
}