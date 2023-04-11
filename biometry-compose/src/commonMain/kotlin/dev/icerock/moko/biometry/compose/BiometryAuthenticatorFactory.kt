/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.biometry.compose

import androidx.compose.runtime.Composable
import dev.icerock.moko.biometry.BiometryAuthenticator

fun interface BiometryAuthenticatorFactory {
    fun createBiometryAuthenticator(): BiometryAuthenticator
}

@Composable
expect fun rememberBiometryAuthenticatorFactory(): BiometryAuthenticatorFactory
