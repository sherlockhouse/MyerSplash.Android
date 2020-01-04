package com.juniperphoton.myersplash.misc

inline fun <R> guard(block: () -> R): R? {
    try {
        return block()
    } catch (e: Exception) {
        // ignored
    }

    return null
}