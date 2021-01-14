/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.biometry

import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError
import platform.LocalAuthentication.LABiometryType
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicy

actual class BiometryAuthenticator actual constructor() {
    actual suspend fun checkBiometryAuthentication(
        requestReason: StringDesc,
        failureButtonText: StringDesc
    ): Boolean {

        val laContext = LAContext()
        laContext.setLocalizedFallbackTitle(failureButtonText.localized())

        val (canEvaluate, error) = memScoped {
            val p = alloc<ObjCObjectVar<NSError?>>()
            val canEvaluate: Boolean? = runCatching {
                laContext.canEvaluatePolicy(AUTH_DEVICE_OWNER_POLICY, error = p.ptr)
            }.getOrNull()
            canEvaluate to p.value
        }

        if (error != null) throw error.toException()

        val reason = requestReason.localized()

        return if (canEvaluate!!) {
            callbackToCoroutine<Boolean> { callback ->
                laContext.evaluatePolicy(
                    policy = AUTH_DEVICE_OWNER_POLICY,
                    localizedReason = reason,
                    reply = mainContinuation { result: Boolean, error: NSError? ->
                        callback(result, error)
                    }
                )
            }
        } else {
            false
        }
    }

    actual fun isTouchIdEnabled(): Boolean {
        val laContext = LAContext()
        val canEvaluate = laContext.canEvaluatePolicy(AUTH_DEVICE_OWNER_BIOMETRY_POLICY, error = null)
        return canEvaluate && laContext.biometryType == BIOMETRY_TYPE_TOUCH_ID
    }

    actual fun isFaceIdEnabled(): Boolean {
        val laContext = LAContext()
        val canEvaluate = laContext.canEvaluatePolicy(AUTH_DEVICE_OWNER_BIOMETRY_POLICY, error = null)
        return canEvaluate && laContext.biometryType == BIOMETRY_TYPE_FACE_ID
    }

    companion object {
        private const val AUTH_DEVICE_OWNER_BIOMETRY_POLICY: Long = 1
        private const val AUTH_DEVICE_OWNER_POLICY: Long = 2

        private const val BIOMETRY_TYPE_TOUCH_ID: Long = 1
        private const val BIOMETRY_TYPE_FACE_ID: Long = 2
    }
}