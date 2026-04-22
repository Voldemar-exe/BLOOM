plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.bloom.room)
    alias(libs.plugins.bloom.koin)
}

android {
    namespace = "com.example.bloom.core.database"
}

dependencies {
    implementation(project(":core:model"))
}
