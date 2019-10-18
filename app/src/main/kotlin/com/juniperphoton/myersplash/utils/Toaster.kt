package com.juniperphoton.myersplash.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.StringRes
import com.juniperphoton.myersplash.App
import com.juniperphoton.myersplash.di.AppComponent

interface Toaster {
    fun showToast(content: String)
    fun showToast(@StringRes content: Int)
    fun cancelToast()
}

object ToasterHelper : Toaster by AppComponent.instance.toaster

class ToasterImpl : Toaster {
    private var currentToast: Toast? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    private val context: Context = App.instance

    override fun showToast(content: String) {
        if (Looper.getMainLooper().thread !== Thread.currentThread()) {
            mainHandler.post { showToastInternal(context, content) }
        } else {
            showToastInternal(context, content)
        }
    }

    override fun showToast(@StringRes content: Int) {
        if (Looper.getMainLooper().thread !== Thread.currentThread()) {
            mainHandler.post { showToastInternal(context, content) }
        } else {
            showToastInternal(context, content)
        }
    }

    override fun cancelToast() {
        if (currentToast != null) {
            currentToast!!.cancel()
        }
    }

    private fun showToastInternal(context: Context, content: String) {
        cancelToast()
        currentToast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
        currentToast!!.show()
    }

    private fun showToastInternal(context: Context, @StringRes content: Int) {
        cancelToast()
        currentToast = Toast.makeText(context, content, Toast.LENGTH_SHORT)
        currentToast!!.show()
    }
}