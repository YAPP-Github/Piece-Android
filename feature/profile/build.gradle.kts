plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.profile"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.lottie.compose)
}
