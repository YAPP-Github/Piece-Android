import java.util.Properties

plugins {
    id("piece.android.application")
    id("piece.android.compose")
}

android {
    namespace = "com.puzzle.piece"

    defaultConfig {
        versionCode = 1
        versionName = "1.0.0"
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").bufferedReader())
        manifestPlaceholders["KAKAO_APP_KEY"] = localProperties["KAKAO_APP_KEY"] as String
        buildConfigField("String", "KAKAO_APP_KEY", "\"${localProperties["KAKAO_APP_KEY"]}\"")
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.mavericks)
    implementation(libs.mavericks.hilt)

    implementation(libs.kakao.user)

    implementation(projects.feature.auth)
    implementation(projects.feature.setting)
    implementation(projects.feature.matching)
    implementation(projects.feature.mypage)

    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.core.commonUi)
}