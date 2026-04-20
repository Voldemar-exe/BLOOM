package com.example.bloom

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion =
                libs.findVersion("androidx-compose-compiler").get().toString()
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
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        }
    }
}