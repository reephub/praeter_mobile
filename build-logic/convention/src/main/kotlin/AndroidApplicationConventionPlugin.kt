import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.reephub.praeter.configureKotlinAndroid
import com.reephub.praeter.configurePrintApksTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
                apply("kotlinx-serialization")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AndroidConfiguration.Sdk.TARGET

                defaultConfig.versionCode = AndroidConfiguration.Application.CODE
                defaultConfig.versionName = AndroidConfiguration.Application.version.toString()
                // configureFlavors(this)

                // Enabling multidex support.
                defaultConfig.multiDexEnabled = true

                defaultConfig.vectorDrawables.useSupportLibrary = true

                buildFeatures {
                    buildConfig = true
                }
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                configurePrintApksTask(this)
            }
        }
    }
}