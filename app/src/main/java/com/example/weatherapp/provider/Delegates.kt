package com.example.weatherapp.provider

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

fun <T> lazyDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy { GlobalScope.async(start = CoroutineStart.LAZY) { block.invoke(this) } }
}
fun <T> eagerDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy { GlobalScope.async(start = CoroutineStart.ATOMIC) { block.invoke(this) } }
}