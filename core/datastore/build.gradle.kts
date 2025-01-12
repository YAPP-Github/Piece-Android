plugins {
    id("piece.android.library")
    id("piece.android.hilt")
}

android {
    namespace = "com.puzzle.datastore"
}

dependencies {
    implementation(libs.androidx.datastore)
}
