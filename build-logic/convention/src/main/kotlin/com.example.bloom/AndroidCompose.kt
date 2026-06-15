package com.example.bloom

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension) {
    commonExtension.apply {
        buildFeatures.apply {
            compose = true
        }

        dependencies {
            val composeBom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(composeBom))
            "androidTestImplementation"(platform(composeBom))

            val koinBom = libs.findLibrary("koin-bom").get()
            "implementation"(platform(koinBom))

            "implementation"(libs.findLibrary("koin-android").get())
            "implementation"(libs.findLibrary("koin-compose").get())
            "implementation"(libs.findLibrary("koin-compose-viewmodel").get())

            "implementation"(libs.findLibrary("androidx-compose-material-icons").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            "implementation"(libs.findLibrary("androidx-compose-ui").get())
            "implementation"(libs.findLibrary("androidx-compose-ui-graphics").get())
            "implementation"(libs.findLibrary("androidx-compose-material3").get())
//            "implementation"(libs.findLibrary("androidx-compose-material3-adaptive").get())
//            "implementation"(libs.findLibrary("androidx-compose-material3-adaptive-layout").get())
//            "implementation"(libs.findLibrary("androidx-compose-material3-window-size-class1").get())
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}
