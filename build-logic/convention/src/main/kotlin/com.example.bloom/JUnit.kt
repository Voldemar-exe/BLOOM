package com.example.bloom

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureJUnit() {
    dependencies {
        "testImplementation"(libs.findLibrary("junit").get())

        "androidTestImplementation"(libs.findLibrary("androidx-junit").get())
        "androidTestImplementation"(libs.findLibrary("androidx-espresso-core").get())
        "androidTestImplementation"(libs.findLibrary("androidx-compose-ui-test-junit4").get())
        "androidTestImplementation"(libs.findLibrary("androidx-test-uiautomator").get())
    }
}