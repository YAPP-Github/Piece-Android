plugins {
    id("piece.android.library")
    id("piece.android.compose")
}

android {
    namespace = "com.puzzle.designsystem"
}

dependencies {
    implementation(projects.core.commonUi)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)
}
