// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    kotlin("android") version "1.9.0" apply false
    kotlin("kapt") version "1.9.0" apply false
}

allprojects {
//    tasks
//        .withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>()
//        .configureEach {
//            compilerOptions
//                .allWarningsAsErrors
//                .set(
//                    true
//                )
////            compilerOptions
////                .freeCompilerArgs
////                .add("-Xlint:deprecation")
//        }
}