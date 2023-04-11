/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.biometry.compose

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.biometry.BiometryAuthenticator

@Composable
actual fun rememberBiometryAuthenticatorFactory(): BiometryAuthenticatorFactory {
    val context: Context = LocalContext.current
    return remember(context) {
        BiometryAuthenticatorFactory {
            BiometryAuthenticator(applicationContext = context.applicationContext)
        }
    }
}
