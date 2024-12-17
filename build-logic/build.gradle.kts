plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "piece.android.hilt"
            implementationClass = "com.puzzle.build.logic.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "piece.kotlin.hilt"
            implementationClass = "com.puzzle.build.logic.HiltKotlinPlugin"
        }
    }
}
