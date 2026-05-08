plugins {
    alias(libs.plugins.bloom.android.library)
}

android {
    namespace = "com.example.bloom.core.network"
    buildTypes {
        debug {
            buildConfigField("String", "API_URL", "\"http://10.0.2.2:8088\"")
        }
    }
}

dependencies {
    api(libs.ktor.client.core)
    api(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(project(":core:datastore"))
}