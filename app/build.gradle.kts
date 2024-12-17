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
    }

    packaging { resources { excludes += "/META-INF/*" } }

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
    implementation(libs.androidx.compose.material3)

    implementation(projects.feature.auth)
    implementation(projects.feature.etc)
    implementation(projects.feature.matching)
    implementation(projects.feature.mypage)

    implementation(projects.core.designsystem)
}