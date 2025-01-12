plugins {
    id("piece.kotlin.library")
    id("piece.kotlin.hilt")
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(projects.core.common)
}
