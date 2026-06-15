package com.example.bloom

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Applies Kotlin configuration settings specific to Android projects.
 * Configures compileSdk, minSdk, and Java compatibility options based on version catalog inputs.
 *
 * @param commonExtension The Android Gradle plugin's CommonExtension object to configure.
 */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension) {
    commonExtension.apply {
        compileSdk =
            libs
                .findVersion("compile-sdk")
                .get()
                .toString()
                .toInt()

        defaultConfig.apply {
            minSdk =
                libs
                    .findVersion("min-sdk")
                    .get()
                    .toString()
                    .toInt()
        }

        compileOptions.apply {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Applies Kotlin configuration settings for non-Android JVM projects.
 * Configures source and target compatibility to Java 11.
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * Configures common Kotlin compiler options for various project types.
 * Sets the JVM target to 11.
 *
 * @receiver The Project object on which the extension function is called.
 * @param T The type of KotlinBaseExtension being configured.
 */
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() =
    configure<T> {
        when (this) {
            is KotlinAndroidProjectExtension -> compilerOptions
            is KotlinJvmProjectExtension -> compilerOptions
            else -> TODO("Unsupported project extension $this ${T::class}")
        }.apply {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
