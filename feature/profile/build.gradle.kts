plugins {
    alias(libs.plugins.bloom.android.feature)
    alias(libs.plugins.bloom.android.library.compose)
}

android {
    namespace = "com.example.bloom.feature.profile"
}

dependencies {
    api(project(":core:navigation"))
}
