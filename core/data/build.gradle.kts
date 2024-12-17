plugins {
    id("piece.android.library")
    id("piece.android.hilt")
}

android {
    namespace = "com.puzzle.data"
}

dependencies {
    implementation(projects.core.domain)
}