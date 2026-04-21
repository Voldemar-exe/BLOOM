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
                "proguard-rules.pro",
            )
        }
    }
}

dependencies {
    implementation(project(":core:navigation"))

    implementation(project(":feature:habit"))
    implementation(project(":feature:task"))
    implementation(project(":feature:stats"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:auth"))
}
