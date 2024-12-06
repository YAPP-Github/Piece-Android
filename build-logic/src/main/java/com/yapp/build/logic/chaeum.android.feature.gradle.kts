import com.yapp.build.logic.configureHiltAndroid
import com.yapp.build.logic.libs

plugins {
    id("chaeum.android.library")
    id("chaeum.android.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()

dependencies {
//    implementation(project(":core:designsystem:compose"))
//    implementation(project(":core:domain"))
//    implementation(project(":core:navigation"))
//    implementation(project(":core:analytics"))
//    implementation(project(":core:common-ui"))

    val libs = project.extensions.libs
    implementation(libs.findLibrary("androidx.compose.navigation").get())
    implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
    implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
}