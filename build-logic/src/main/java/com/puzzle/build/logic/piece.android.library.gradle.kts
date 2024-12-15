import com.puzzle.build.logic.configureHiltAndroid
import com.puzzle.build.logic.configureKotlinAndroid
import com.puzzle.build.logic.configureTestAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureTestAndroid()
configureHiltAndroid()