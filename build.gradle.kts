// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    // ext.kotlin_version = "1.5.31"
    // This version (1.0.1) of the Compose Compiler requires Kotlin version 1.5.21
    // but you appear to be using Kotlin version 1.5.31 which is not known to be compatible.
    val kotlin_version = "1.8.10"
    val hiltVersion = "2.46.1"
    val navigationVersion = "2.5.0"

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:8.0.2")

        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:$kotlin_version")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files

        // Hilt
        classpath ("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")

        // Google Services
        classpath ("com.google.gms:google-services:4.3.15")
        // Add the Crashlytics Gradle plugin (be sure to add version
        // 2.0.0 or later if you built your app with Android Studio 4.1).
        classpath ("com.google.firebase:firebase-crashlytics-gradle:2.9.6")

        // Navigation
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
    }
}


@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.secrets) apply false
    // Google Services
    alias(libs.plugins.playservices) apply false
    //  Crashlytics Gradle plugin
    alias(libs.plugins.crashlytics) apply false
    // Performance Monitoring plugin
    alias(libs.plugins.performances) apply false
    // Navigation
    alias(libs.plugins.navigation) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

tasks.register("printVersionName") {
    doLast {
        // print(AndroidConfiguration.)
    }
}