import com.example.bloom.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "bloom.android.library")
            apply(plugin = "bloom.koin")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "implementation"(project(":core:designsystem"))
                "implementation"(libs.findLibrary("androidx-lifecycle-viewModel-compose").get())
//                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
//                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                "implementation"(libs.findLibrary("androidx-compose-ui").get())
                "implementation"(libs.findLibrary("androidx-compose-ui-graphics").get())
                "implementation"(libs.findLibrary("androidx-compose-material3").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                "implementation"(libs.findLibrary("timber").get())
                "testImplementation"(libs.findLibrary("junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx-junit").get())
                "androidTestImplementation"(libs.findLibrary("androidx-espresso-core").get())
//                "testImplementation"(libs.findLibrary("androidx.navigation.testing").get())
//                "androidTestImplementation"(
//                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get(),
//                )
            }
        }
    }
}
