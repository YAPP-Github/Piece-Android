plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.presentation"
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.ui)

    implementation(projects.feature.auth)
    implementation(projects.feature.setting)
    implementation(projects.feature.matching)
    implementation(projects.feature.mypage)
}