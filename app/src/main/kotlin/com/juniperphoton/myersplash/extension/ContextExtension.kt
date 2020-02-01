package com.juniperphoton.myersplash.extension

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.juniperphoton.myersplash.R

private var isPad: Boolean? = null

fun Context.isPad(): Boolean {
    if (isPad != null) {
        return isPad!!
    }
    isPad =
            (this.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE
    return isPad!!
}

fun Context.getScreenWidth(): Int = resources.displayMetrics.widthPixels

fun Context.getScreenHeight(): Int = resources.displayMetrics.heightPixels

fun Context.getStatusBarHeight(): Int {
    return try {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            throw IllegalArgumentException("no res id for status bar height")
        }
    } catch (e: Exception) {
        resources.getDimensionPixelSize(R.dimen.status_bar_height)
    }
}

fun Context.getNavigationBarSize(): Point {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        Point(getScreenWidth(), resources.getDimensionPixelSize(resourceId))
    } else Point(0, 0)
}

fun Context.usingWifi(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        checkWifiAPI28(manager)
    } else {
        checkWifiAPIPre28(manager)
    }
}

@TargetApi(Build.VERSION_CODES.P)
private fun checkWifiAPI28(manager: ConnectivityManager): Boolean {
    val network = manager.activeNetwork
    val cap = manager.getNetworkCapabilities(network)
    return cap?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: return false
}

@Suppress("DEPRECATION")
private fun checkWifiAPIPre28(manager: ConnectivityManager): Boolean {
    val info = manager.activeNetworkInfo
    return info?.type == ConnectivityManager.TYPE_WIFI
}

@Suppress("unused")
fun Context.getVersionName(): String? {
    return try {
        val manager = packageManager
        val info = manager.getPackageInfo(packageName, 0)
        "Version ${info.versionName}"
    } catch (e: Exception) {
        null
    }
}

fun Context.startActivitySafely(intent: Intent) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

fun Context.startServiceSafely(intent: Intent) {
    try {
        startService(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context?.getSpanCount(): Int {
    this ?: return 1

    val width = this.getScreenWidth()

    if (!this.isPad()) {
        return when (resources.configuration.orientation) {
            ORIENTATION_PORTRAIT -> {
                1
            }
            else -> {
                3
            }
        }
    }

    return when (width) {
        in 0 until 1500 -> {
            2
        }
        in 1500 until 2000 -> {
            3
        }
        in 2000 until 3000 -> {
            4
        }
        else -> {
            5
        }
    }
}