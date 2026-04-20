plugins {
    alias(libs.plugins.bloom.android.feature)
    alias(libs.plugins.bloom.android.library.compose)
    alias(libs.plugins.bloom.koin)
}

android {
    namespace = "com.example.bloom.feature.auth"
}

dependencies {
    api(project(":core:navigation"))
    api(project(":core:data"))
    api(project(":core:database"))
    api(project(":core:network"))
}