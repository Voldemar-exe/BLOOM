plugins {
    alias(libs.plugins.bloom.android.library)
    alias(libs.plugins.protobuf)
}

protobuf {
    protoc {
        artifact =
            libs.protobuf.protoc
                .get()
                .toString()
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                create("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

android {
    namespace = "com.example.bloom.core.datastore"
}

dependencies {
    implementation(project(":core:model"))
    api(libs.protobuf.kotlin.lite)
    api(libs.androidx.dataStore)
}
