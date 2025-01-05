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
    implementation(libs.mavericks)
    implementation(libs.mavericks.hilt)
    implementation(libs.kakao.user)

    implementation(projects.presentation)
    implementation(projects.core.data)
}