package com.juniperphoton.myersplash.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.juniperphoton.myersplash.R

/**
 * @author JuniperPhoton @ Zhihu Inc.
 * @since 2019-09-10
 */
object ThemeHelper {
    fun switchTheme(context: Context) {
        val theme = LocalSettingHelper.getInt(context, context.getString(R.string.preference_key_theme), 2)
        AppCompatDelegate.setDefaultNightMode(when (theme) {
            0 -> AppCompatDelegate.MODE_NIGHT_YES
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        })
    }

    fun isDark(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }
}