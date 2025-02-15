import java.util.Properties

plugins {
    id("piece.android.feature")
}

android {
    namespace = "com.puzzle.setting"

    defaultConfig {
        val localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").bufferedReader())
        buildConfigField(
            type = "String",
            name = "PIECE_CHANNEL_TALK_URL",
            value = "\"${localProperties["PIECE_CHANNEL_TALK_URL"]}\""
        )
        buildConfigField(
            type = "String",
            name = "PIECE_TERMS_OF_USE_URL",
            value = "\"${localProperties["PIECE_TERMS_OF_USE_URL"]}\""
        )
        buildConfigField(
            type = "String",
            name = "PIECE_PRIVACY_AND_POLICY_URL",
            value = "\"${localProperties["PIECE_PRIVACY_AND_POLICY_URL"]}\""
        )
        buildConfigField(
            type = "String",
            name = "PIECE_NOTICE_URL",
            value = "\"${localProperties["PIECE_NOTICE_URL"]}\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)
}
