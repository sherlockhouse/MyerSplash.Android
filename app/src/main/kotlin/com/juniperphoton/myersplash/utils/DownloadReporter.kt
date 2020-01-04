package com.juniperphoton.myersplash.utils

import com.juniperphoton.myersplash.api.IOService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Report download behavior to Unsplash server.
 */
class DownloadReporter @Inject constructor(private val service: IOService) {
    companion object {
        private const val TAG = "DownloadReporter"
    }

    fun report(downloadLocation: String?) {
        val url = downloadLocation ?: return

        GlobalScope.launch(context = CoroutineExceptionHandler { _, _ ->
            // ignored
        }) {
            service.reportDownload(url)
            Pasteur.info(TAG, "successfully report $url")
        }
    }
}