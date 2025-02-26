plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.matching"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.cloudy.compose)
}
