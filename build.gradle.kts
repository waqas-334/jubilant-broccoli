
// Top-level build file where you can add configuration options common to all sub-projects/modules.
//
//buildscript {
//    repositories {
//        // other repositories...
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        // other plugins...
//        classpath("com.android.tools.build:gradle:8.5.2")
////        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.52")
////        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
////        classpath(libs.plugins.compose.compiler)
//    }
//}
//

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(libs.gradle)
    }
}
plugins {
//    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.hilt.android) apply false
}