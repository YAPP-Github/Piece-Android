plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.profile"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
}
