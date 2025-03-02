import java.util.Properties

plugins {
    id("piece.android.library")
    id("piece.android.compose")
}

android {
    namespace = "com.puzzle.analytics"

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").bufferedReader())
        buildConfigField(
            "String",
            "AMPLITUDE_API_KEY",
            "\"${properties["AMPLITUDE_API_KEY"]}\"",
        )
    }

    buildFeatures{
        buildConfig = true
    }
}

dependencies {
    implementation(libs.amplitude.analytics)
}
