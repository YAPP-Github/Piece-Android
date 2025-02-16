import java.util.Properties

plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.presentation"

    defaultConfig {
        val localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").bufferedReader())
        buildConfigField("String", "PIECE_MARKET_URL", "\"${localProperties["PIECE_MARKET_URL"]}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.ui)

    implementation(projects.feature.onboarding)
    implementation(projects.feature.auth)
    implementation(projects.feature.setting)
    implementation(projects.feature.matching)
    implementation(projects.feature.profile)
}
