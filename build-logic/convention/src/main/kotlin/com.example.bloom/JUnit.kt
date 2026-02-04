package com.example.bloom

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureJUnit() {
    dependencies {
        "testImplementation"(libs.findLibrary("junit").get())
    }
}