plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.bloom.android.library.compose)
}

android {
    namespace = "com.example.bloom.core.ui"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    "implementation"("androidx.compose.material3:material3:1.5.0-alpha17")
}