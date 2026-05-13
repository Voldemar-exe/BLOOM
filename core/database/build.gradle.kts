plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.bloom.room)
}

android {
    namespace = "com.example.bloom.core.database"
}

dependencies {
    implementation(project(":core:model"))

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
