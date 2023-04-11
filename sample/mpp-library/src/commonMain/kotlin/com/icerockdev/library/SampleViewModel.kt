/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */
package com.icerockdev.library

import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch

class SampleViewModel(
    override val eventsDispatcher: EventsDispatcher<EventListener>
) : ViewModel(), EventsDispatcherOwner<SampleViewModel.EventListener> {

    val biometryAuthenticator = BiometryAuthenticator()

    @Suppress("TooGenericExceptionCaught")
    fun tryToAuth() {
        viewModelScope.launch {
            try {
                val isSuccess = biometryAuthenticator.checkBiometryAuthentication(
                    requestReason = "Just for test".desc(),
                    failureButtonText = "Oops".desc()
                )
                if (isSuccess) {
                    eventsDispatcher.dispatchEvent { onSuccess() }
                }
            } catch (throwable: Throwable) {
                println(throwable)
                eventsDispatcher.dispatchEvent { onFail() }
            }
        }
    }

    interface EventListener {
        fun onSuccess()

        fun onFail()
    }
}
