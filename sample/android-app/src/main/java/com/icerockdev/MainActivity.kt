/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.icerockdev.library.SampleViewModel
import dev.icerock.moko.biometry.BiometryAuthenticator
import dev.icerock.moko.mvvm.dispatcher.eventsDispatcherOnMain
import dev.icerock.moko.mvvm.getViewModel

class MainActivity : AppCompatActivity(), SampleViewModel.EventListener {

    private lateinit var viewModel: SampleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creates viewModel from common code.
        viewModel = getViewModel {
            SampleViewModel(
                biometryAuthenticator = BiometryAuthenticator(applicationContext = applicationContext),
                eventsDispatcher = eventsDispatcherOnMain()
            )
        }.also {
            it.eventsDispatcher.bind(this, this)
            it.biometryAuthenticator.bind(
                lifecycle = this.lifecycle,
                fragmentManager = supportFragmentManager
            )
        }
    }

    fun onLoginButtonClick(@Suppress("UNUSED_PARAMETER") view: View?) {
        viewModel.tryToAuth()
    }

    override fun onSuccess() {
        showToast("Login successfully!")
    }

    override fun onFail() {
        showToast("Ooops. Login failed!")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
