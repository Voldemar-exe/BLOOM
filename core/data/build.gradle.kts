plugins {
    alias(libs.plugins.bloom.android.library)
}

android {
    namespace = "com.example.bloom.core.data"
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
}