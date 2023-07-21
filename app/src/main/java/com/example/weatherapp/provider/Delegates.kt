package com.example.weatherapp.provider

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

fun <T> lazyDeferred(lifecycleOwner: LifecycleOwner, block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        lifecycleOwner.lifecycleScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}
