plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.auth"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.kakao.user)
    implementation(libs.accompanist.permission)
}
