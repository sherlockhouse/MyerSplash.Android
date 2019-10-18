package com.juniperphoton.myersplash.utils

import androidx.annotation.UiThread

/**
 * @author JuniperPhoton @ Zhihu Inc.
 * @since 2019-10-16
 */
data class LiveDataEvent<T>(private val data: T) {
    private var handled = false

    @UiThread
    fun peek() = data

    @UiThread
    fun pop(): T? = if (!handled) {
        handled = true
        data
    } else {
        null
    }

    @UiThread
    inline fun consume(block: (T) -> Unit) {
        pop()?.let {
            block(it)
        }
    }
}

fun <T> T.toLiveDataEvent(): LiveDataEvent<T> {
    return LiveDataEvent(this)
}

val <T> T.liveDataEvent: LiveDataEvent<T>
    get() = LiveDataEvent(this)