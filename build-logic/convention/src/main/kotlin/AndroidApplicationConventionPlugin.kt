import com.example.bloom.applicationExtension
import com.example.bloom.configureJUnit
import com.example.bloom.configureKotlin
import com.example.bloom.configureKotlinAndroid
import com.example.bloom.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "bloom.koin")
            apply(plugin = "org.jetbrains.kotlin.android")

            requireNotNull(applicationExtension).apply {
                buildFeatures {
                    buildConfig = true
                }

                configureKotlin()
                configureKotlinAndroid(this)
                defaultConfig.targetSdk =
                    libs
                        .findVersion("target-sdk")
                        .get()
                        .toString()
                        .toInt()
            }

            dependencies {
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
                "implementation"(libs.findLibrary("androidx-activity-compose").get())
                "implementation"(libs.findLibrary("androidx-navigation3-ui").get())
                "implementation"(libs.findLibrary("timber").get())
            }
            configureJUnit()
        }
    }
}
