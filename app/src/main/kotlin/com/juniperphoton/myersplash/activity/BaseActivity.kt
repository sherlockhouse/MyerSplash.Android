package com.juniperphoton.myersplash.activity

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.juniperphoton.myersplash.R
import com.juniperphoton.myersplash.extension.getStatusBarHeight
import com.juniperphoton.myersplash.extension.updateDimensions
import com.juniperphoton.myersplash.utils.Pasteur

abstract class BaseActivity : AppCompatActivity(), View.OnApplyWindowInsetsListener, View.OnClickListener {
    companion object {
        private const val TAG = "BaseActivity"
    }

    private var systemUiConfigured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.TRANSPARENT

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        updateStatusBar(currentNightMode == Configuration.UI_MODE_NIGHT_NO)
    }

    protected fun updateStatusBar(darkText: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var prev = decorView.systemUiVisibility
            prev = if (darkText) {
                prev or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                prev and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                prev = prev or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }
            decorView.systemUiVisibility = prev
        }
    }

    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            val enter = resources.getIdentifier("android:activity_close_enter", "anim", null)
            val exit = resources.getIdentifier("android:activity_close_exit", "anim", null)
            if (enter != 0 && exit != 0) {
                overridePendingTransition(enter, exit)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val content = findViewById<View>(android.R.id.content)
        content.setOnApplyWindowInsetsListener(this)

        if (!systemUiConfigured) {
            systemUiConfigured = true
            onConfigStatusBar()
        }
    }

    open fun onConfigStatusBar() {
        // todo fix this
        findViewById<View>(R.id.status_bar_placeholder)?.updateDimensions(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight())
    }

    open fun onApplySystemInsets(top: Int, bottom: Int) = Unit

    override fun onClick(v: View) {
        onClickView(v)
    }

    open fun onClickView(v: View) = Unit

    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        Pasteur.info(TAG, "height: ${insets.systemWindowInsetBottom}")
        onApplySystemInsets(insets.systemWindowInsetTop, insets.systemWindowInsetBottom)
        return insets.consumeSystemWindowInsets()
    }
}
