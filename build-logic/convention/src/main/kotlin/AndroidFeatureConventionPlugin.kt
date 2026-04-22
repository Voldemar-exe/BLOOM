import com.example.bloom.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "bloom.android.library")

            dependencies {
                "implementation"(project(":core:model"))
                "implementation"(project(":core:data"))
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:ui"))
                "implementation"(libs.findLibrary("androidx-lifecycle-viewModel-compose").get())
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
