plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.bloo.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation3.ui)
    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
}