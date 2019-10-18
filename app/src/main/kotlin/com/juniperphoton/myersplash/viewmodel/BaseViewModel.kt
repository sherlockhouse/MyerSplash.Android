package com.juniperphoton.myersplash.viewmodel

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import com.juniperphoton.myersplash.di.AppComponent
import com.juniperphoton.myersplash.utils.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import javax.inject.Inject

/**
 * @author JuniperPhoton @ Zhihu Inc.
 * @since 2019-10-16
 */
abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
        CoroutineScope by MainScope() {
    @Inject
    lateinit var toaster: Toaster

    fun getString(@StringRes stringRes: Int): String {
        return getApplication<Application>().getString(stringRes)
    }

    fun getString(@StringRes stringRes: Int, vararg params: Any): String {
        return getApplication<Application>().getString(stringRes, *params)
    }

    fun showToast(@StringRes stringRes: Int) {
        toaster.showToast(getString(stringRes))
    }

    fun showToast(text: String?) {
        text ?: return
        toaster.showToast(text)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : BaseViewModel> inject(): T {
        when (this) {
            is ImageListViewModel -> {
                AppComponent.instance.inject(this)
            }
        }
        return this as T
    }
}