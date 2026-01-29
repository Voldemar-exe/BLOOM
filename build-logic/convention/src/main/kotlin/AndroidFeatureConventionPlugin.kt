import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "bloom.android.library")
//            apply(plugin = "nowinandroid.hilt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")


            dependencies {
//                "implementation"(project(":core:ui"))
//                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewModel-compose").get())
//                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
//                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                "implementation"(libs.findLibrary("timber").get())
//                "testImplementation"(libs.findLibrary("androidx.navigation.testing").get())
//                "androidTestImplementation"(
//                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get(),
//                )
            }
        }
    }
}
