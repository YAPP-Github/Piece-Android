plugins {
    id("chaeum.android.application")
    id("chaeum.android.compose")
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.yapp.chaeum"

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

    implementation(projects.feature.auth)
    implementation(projects.feature.etc)
    implementation(projects.feature.matching)
    implementation(projects.feature.mypage)
}