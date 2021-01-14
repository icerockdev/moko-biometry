/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    plugin(Deps.Plugins.androidLibrary)
    plugin(Deps.Plugins.kotlinMultiPlatform)
    plugin(Deps.Plugins.mobileMultiPlatform)
    plugin(Deps.Plugins.androidExtensions)
    plugin(Deps.Plugins.iosFramework)
}

dependencies {
    commonMainApi(Deps.Libs.MultiPlatform.mokoBiometry)
    commonMainApi(Deps.Libs.MultiPlatform.coroutines)
    mppLibrary(Deps.Libs.MultiPlatform.mokoMvvm)
}
