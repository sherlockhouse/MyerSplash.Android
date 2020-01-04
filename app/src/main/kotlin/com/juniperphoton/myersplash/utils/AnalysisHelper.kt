package com.juniperphoton.myersplash.utils

import com.microsoft.appcenter.analytics.Analytics

interface AnalysisHelper {
    fun logTabSelected(name: String)
    fun logClickCopyUrl()
    fun logEnterDownloads()
    fun logDownloadSuccess(durationMs: Long)
    fun logDownloadFailed(e: Exception, durationMs: Long)
    fun logEnterSearch()
    fun logClickDownloadInList()
    fun logClickDownloadInDetails()
    fun logClickCancelDownloadInDetails()
    fun logClickSetAsInDetails()
    fun logClickSetAsInDownloadList()
    fun logToggleImageDetails()
    fun logRefreshList()
    fun logApplyEdit(dimProgress: Boolean)
    fun logEditShowPreview()
    fun logClickMoreButtonInDownloadList()
}

class AnalysisHelperImpl : AnalysisHelper {
    override fun logTabSelected(name: String) {
        Analytics.trackEvent("Tab selected", mapOf("Name" to name))
    }

    override fun logClickCopyUrl() {
        Analytics.trackEvent("URL copied")
    }

    override fun logEnterDownloads() {
        Analytics.trackEvent("Enter downloads")
    }

    override fun logDownloadSuccess(durationMs: Long) {
        Analytics.trackEvent("Download success", mapOf("Duration" to durationMs.toString()))
    }

    override fun logDownloadFailed(e: Exception, durationMs: Long) {
        Analytics.trackEvent("Download failed",
                mapOf("Duration" to durationMs.toString(), "Error" to e.toString()))
    }

    override fun logEnterSearch() {
        Analytics.trackEvent("Enter search")
    }

    override fun logClickDownloadInList() {
        Analytics.trackEvent("Click download in list")
    }

    override fun logClickDownloadInDetails() {
        Analytics.trackEvent("Click download in details")
    }

    override fun logClickCancelDownloadInDetails() {
        Analytics.trackEvent("Click cancel download in details")
    }

    override fun logClickSetAsInDetails() {
        Analytics.trackEvent("Click set-as button in details")
    }

    override fun logClickSetAsInDownloadList() {
        Analytics.trackEvent("Click set-as button in download list")
    }

    override fun logToggleImageDetails() {
        Analytics.trackEvent("Toggle image details")
    }

    override fun logRefreshList() {
        Analytics.trackEvent("Refresh list")
    }

    override fun logApplyEdit(dimProgress: Boolean) {
        Analytics.trackEvent("Apply edit", mapOf("Dim progress" to dimProgress.toString()))
    }

    override fun logEditShowPreview() {
        Analytics.trackEvent("Edit show preview")
    }

    override fun logClickMoreButtonInDownloadList() {
        Analytics.trackEvent("Click more button in download list")
    }
}