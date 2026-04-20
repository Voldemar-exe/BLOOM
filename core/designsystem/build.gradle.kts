plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.bloom.android.library.compose)
}

android {
    namespace = "com.example.bloom.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.icons)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
}