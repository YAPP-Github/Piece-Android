plugins {
    id("piece.android.library")
    id("piece.android.hilt")
}

android {
    namespace = "com.puzzle.database"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)

    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
