package com.juniperphoton.myersplash.utils

import androidx.annotation.IntDef

/**
 * @author JuniperPhoton @ Zhihu Inc.
 * @since 2019-10-16
 */

data class Response(@Status val status: Int) {
    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_FAILED = 1
        const val STATUS_HIDDEN = 2

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(STATUS_LOADING, STATUS_FAILED, STATUS_HIDDEN)
        annotation class Status
    }
}