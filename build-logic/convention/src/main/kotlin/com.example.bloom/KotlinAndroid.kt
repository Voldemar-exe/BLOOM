package com.example.bloom

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure

internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk =
            libs
                .findVersion("compile-sdk")
                .get()
                .toString()
                .toInt()

        defaultConfig {
            minSdk =
                libs
                    .findVersion("min-sdk")
                    .get()
                    .toString()
                    .toInt()
        }

        compileOptions {
            val javaVersion = JavaVersion.toVersion(libs.findVersion("jvm").get())

            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }
}

internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configureKotlin()
}
