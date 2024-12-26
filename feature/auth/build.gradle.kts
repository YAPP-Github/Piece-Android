plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.auth"
}

dependencies {
    implementation(libs.kakao.user)
}
