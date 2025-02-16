plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.matching"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.cloudy.compose)
}
