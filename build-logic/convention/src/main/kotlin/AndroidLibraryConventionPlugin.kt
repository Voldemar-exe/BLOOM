
import com.example.bloom.configureJUnit
import com.example.bloom.configureKotlinAndroid
import com.example.bloom.libraryExtension
import com.example.bloom.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
            apply(plugin = "bloom.koin")

            requireNotNull(libraryExtension).apply {
                buildFeatures {
                    buildConfig = true
                    defaultConfig.testInstrumentationRunner =
                        "androidx.test.runner.AndroidJUnitRunner"
                }

                configureKotlinAndroid(this)
            }

            dependencies {
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                "implementation"(libs.findLibrary("timber").get())
                "implementation"(libs.findLibrary("core-ktx").get())
                "implementation"(libs.findLibrary("kotlinx-coroutines-android").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("robolectric").get())
                "testImplementation"(libs.findLibrary("mockk").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("slf4j-simple").get())
            }

            configureJUnit()
        }
    }
}
