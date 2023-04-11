/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.biometry.compose

import androidx.compose.runtime.Composable
import dev.icerock.moko.biometry.BiometryAuthenticator

// on iOS side we should not do anything to prepare BiometryAuthenticator to work
@Suppress("FunctionNaming")
@Composable
actual fun BindBiometryAuthenticatorEffect(biometryAuthenticator: BiometryAuthenticator) = Unit
