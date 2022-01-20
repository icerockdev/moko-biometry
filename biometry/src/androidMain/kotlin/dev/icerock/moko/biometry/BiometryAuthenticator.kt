/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.biometry

import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import java.util.concurrent.Executor
import kotlin.coroutines.suspendCoroutine

actual class BiometryAuthenticator actual constructor() {

    var fragmentManager: FragmentManager? = null
    private var _packageManager: PackageManager? = null

    fun bind(lifecycle: Lifecycle, fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager

        val observer = object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed(source: LifecycleOwner) {
                this@BiometryAuthenticator.fragmentManager = null
                source.lifecycle.removeObserver(this)
            }
        }
        lifecycle.addObserver(observer)
    }

    fun setPackageManager(packageManager: PackageManager) {
        this._packageManager = packageManager
    }

    actual suspend fun checkBiometryAuthentication(
        requestReason: StringDesc,
        failureButtonText: StringDesc
    ): Boolean {

        val fragmentManager =
            fragmentManager
                ?: throw IllegalStateException("can't check biometry without active window")

        val currentFragment: Fragment? = fragmentManager.findFragmentByTag(BIOMETRY_RESOLVER_FRAGMENT_TAG)
        val resolverFragment: ResolverFragment = if (currentFragment != null) {
            currentFragment as ResolverFragment
        } else {
            ResolverFragment().apply {
                fragmentManager
                    .beginTransaction()
                    .add(this, BIOMETRY_RESOLVER_FRAGMENT_TAG)
                    .commitNow()
            }
        }

        return suspendCoroutine<Boolean> { continuation ->
            var resumed = false
            resolverFragment.showBiometricPrompt(
                requestTitle = "Biometry".desc(),
                requestReason = requestReason,
                failureButtonText = failureButtonText,
                credentialAllowed = true
            ) {
                if (!resumed) {
                    continuation.resumeWith(it)
                    resumed = true
                }
            }
        }
    }

    /**
     * Performs a fingerprint scan availability check
     *
     * @return true if it is possible to use a fingerprint scanner, false - if it is not available
     */
    actual fun isTouchIdEnabled(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return _packageManager?.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
            ?: throw IllegalStateException("can't check touch id enabled without packageManager")
    }

    /**
     * Performs the availability check of the FaceID scan
     *
     * @return true if it is possible to use the face scanner, false - if it is not available
     */
    actual fun isFaceIdEnabled(): Boolean {
        return false
    }

    class ResolverFragment : Fragment() {

        private lateinit var executor: Executor
        private lateinit var biometricPrompt: BiometricPrompt
        private lateinit var promptInfo: BiometricPrompt.PromptInfo

        init {
            retainInstance = true
        }

        /**
         * Prepare and show BiometricPrompt system dialog
         *
         * @param requestTitle biometric prompt title
         * @param requestReason biometric prompt reason
         * @param failureButtonText
         * @param credentialAllowed Allows user to authenticate using their lock screen PIN, pattern, or password.
         */
        fun showBiometricPrompt(
            requestTitle: StringDesc,
            requestReason: StringDesc,
            failureButtonText: StringDesc,
            credentialAllowed: Boolean,
            callback: (Result<Boolean>) -> Unit
        ) {
            val context = requireContext()

            executor = ContextCompat.getMainExecutor(context)

            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                            errorCode == BiometricPrompt.ERROR_USER_CANCELED
                        ) {
                            callback.invoke(Result.success(false))
                        } else {
                            callback.invoke(Result.failure(Exception(errString.toString())))
                        }
                    }

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        super.onAuthenticationSucceeded(result)
                        callback.invoke(Result.success(true))
                    }
                }
            )

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(requestTitle.toString(context))
                .setSubtitle(requestReason.toString(context))
                .apply {
                    if (!credentialAllowed) {
                        this.setNegativeButtonText(failureButtonText.toString(context))
                    }
                }
                .setDeviceCredentialAllowed(credentialAllowed)
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    companion object {
        private const val BIOMETRY_RESOLVER_FRAGMENT_TAG = "BiometryControllerResolver"
    }
}
