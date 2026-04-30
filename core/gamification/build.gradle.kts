plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.bloom.android.library.compose)
}

android {
    namespace = "com.example.bloom.core.gamification"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
}