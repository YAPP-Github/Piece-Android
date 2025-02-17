import java.util.Properties

plugins {
    id("piece.android.application")
    id("piece.android.compose")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.puzzle.piece"

    defaultConfig {
        versionCode = 2
        versionName = "1.0.1"
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").bufferedReader())
        manifestPlaceholders["KAKAO_APP_KEY"] = localProperties["KAKAO_APP_KEY"] as String
        buildConfigField("String", "KAKAO_APP_KEY", "\"${localProperties["KAKAO_APP_KEY"]}\"")
    }

    signingConfigs {
        create("release") {
            val keystoreProperties = Properties()
            keystoreProperties.load(project.rootProject.file("keystore.properties").bufferedReader())

            storeFile = file(keystoreProperties["STORE_FILE_PATH"] as String)
            storePassword = keystoreProperties["STORE_PASSWORD"] as String
            keyAlias = keystoreProperties["KEY_ALIAS"] as String
            keyPassword = keystoreProperties["KEY_PASSWORD"] as String
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
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
