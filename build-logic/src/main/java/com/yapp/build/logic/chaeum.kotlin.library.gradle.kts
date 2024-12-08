import com.yapp.build.logic.configureKotlin
import com.yapp.build.logic.configureTest

plugins {
    kotlin("jvm")
}

configureKotlin()
configureTest()