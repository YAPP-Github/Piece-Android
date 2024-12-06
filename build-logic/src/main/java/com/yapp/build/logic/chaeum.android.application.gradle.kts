import com.yapp.build.logic.configureHiltAndroid
import com.yapp.build.logic.configureKotlinAndroid
import com.yapp.build.logic.configureTestAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureTestAndroid()