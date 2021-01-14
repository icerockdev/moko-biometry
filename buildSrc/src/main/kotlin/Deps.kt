/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

object Deps {
    private const val kotlinVersion = "1.4.21"
    private const val androidAppCompatVersion = "1.1.0"
    private const val mokoResourcesVersion = "0.14.0"
    private const val androidBiometricVersion = "1.0.1"
    private const val coroutinesVersion = "1.4.2-native-mt"
    private const val mokoMvvmVersion = "0.9.0"
    const val mokoBiometryVersion = "0.1.0"


    object Android {
        const val compileSdk = 28
        const val targetSdk = 28
        const val minSdk = 16
    }

    object Plugins {
        val androidExtensions = GradlePlugin(
            id = "kotlin-android-extensions",
            module = "org.jetbrains.kotlin:kotlin-android-extensions:$kotlinVersion"
        )
        val androidLibrary = GradlePlugin(id = "com.android.library")
        val androidApplication = GradlePlugin(id = "com.android.application")
        val kotlinMultiPlatform = GradlePlugin(id = "org.jetbrains.kotlin.multiplatform")
        val kotlinAndroid = GradlePlugin(id = "kotlin-android")
        val kotlinKapt = GradlePlugin(id = "kotlin-kapt")
        val mobileMultiPlatform = GradlePlugin(id = "dev.icerock.mobile.multiplatform")
        val iosFramework = GradlePlugin(id = "dev.icerock.mobile.multiplatform.ios-framework")
        val mavenPublish = GradlePlugin(id = "maven-publish")
    }

    object Libs {
        object Android {
            val appCompat = AndroidLibrary(
                name = "androidx.appcompat:appcompat:$androidAppCompatVersion"
            )

            val biometric = AndroidLibrary(
                name = "androidx.biometric:biometric:$androidBiometricVersion"
            )

        }

        object MultiPlatform {
            const val mokoBiometry =
                "dev.icerock.moko:biometry:$mokoBiometryVersion"
            const val coroutines =
                    "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"

            val mokoMvvm = MultiPlatformLibrary(
                    common = "dev.icerock.moko:mvvm:$mokoMvvmVersion",
                    iosArm64 = "dev.icerock.moko:mvvm-iosarm64:$mokoMvvmVersion",
                    iosX64 = "dev.icerock.moko:mvvm-iosx64:$mokoMvvmVersion"
            )

            val mokoResources = MultiPlatformLibrary(
                    common = "dev.icerock.moko:resources:$mokoResourcesVersion",
                    iosX64 = "dev.icerock.moko:resources-iosx64:$mokoResourcesVersion",
                    iosArm64 = "dev.icerock.moko:resources-iosarm64:$mokoResourcesVersion"
            )
        }
    }
}
