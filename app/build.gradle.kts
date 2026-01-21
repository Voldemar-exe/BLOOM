plugins {
    alias(libs.plugins.bloom.android.application)
    alias(libs.plugins.bloom.android.application.compose)
}

android {
    namespace = "com.example.bloom"

    defaultConfig {
        applicationId = "com.example.bloom"
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}