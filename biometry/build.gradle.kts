/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    plugin(Deps.Plugins.androidLibrary)
    plugin(Deps.Plugins.kotlinMultiPlatform)
    plugin(Deps.Plugins.androidExtensions)
    plugin(Deps.Plugins.mobileMultiPlatform)
    plugin(Deps.Plugins.mavenPublish)
}

group = "dev.icerock.moko"
version = Deps.mokoBiometryVersion

dependencies {
    androidLibrary(Deps.Libs.Android.appCompat)
    androidLibrary(Deps.Libs.Android.biometric)

    mppLibrary(Deps.Libs.MultiPlatform.mokoResources)
}


publishing {
    repositories.maven("https://api.bintray.com/maven/icerockdev/moko/moko-biometry/;publish=1") {
        name = "bintray"

        credentials {
            username = System.getProperty("BINTRAY_USER")
            password = System.getProperty("BINTRAY_KEY")
        }
    }
}

