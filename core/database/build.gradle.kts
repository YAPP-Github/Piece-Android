plugins {
    id("piece.android.library")
    id("piece.android.hilt")
}

android {
    namespace = "com.puzzle.database"
}

dependencies {
    implementation(projects.core.domain)
    
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
