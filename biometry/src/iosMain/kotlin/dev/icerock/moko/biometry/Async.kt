/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.biometry

import platform.Foundation.NSThread

internal inline fun <T1, T2> mainContinuation(
    noinline block: (T1, T2) -> Unit
): (T1, T2) -> Unit = { arg1, arg2 ->
    if (NSThread.isMainThread()) {
        block.invoke(arg1, arg2)
    } else {
        MainRunDispatcher.run {
            block.invoke(arg1, arg2)
        }
    }
}
