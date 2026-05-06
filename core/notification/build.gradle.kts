plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.bloom.android.library.compose)
}

android {
    namespace = "com.example.bloom.core.notification"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
}
