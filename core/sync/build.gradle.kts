plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.bloom.android.library.compose)
}

android {
    namespace = "com.example.bloom.core.sync"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:network"))
    implementation(libs.koin.androidx.workmanager)
}