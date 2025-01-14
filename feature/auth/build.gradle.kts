plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.auth"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.kakao.user)
    implementation(libs.accompanist.permission)
}
