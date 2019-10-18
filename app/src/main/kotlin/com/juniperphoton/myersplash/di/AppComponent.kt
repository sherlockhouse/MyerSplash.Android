package com.juniperphoton.myersplash.di

import com.juniperphoton.myersplash.utils.Toaster
import com.juniperphoton.myersplash.viewmodel.ImageListViewModel
import dagger.Component

/**
 * @author JuniperPhoton @ Zhihu Inc.
 * @since 2019-10-16
 */
@Component(modules = [ImageModule::class])
interface AppComponent {
    companion object {
        val instance: AppComponent by lazy {
            DaggerAppComponent.builder().build()
        }
    }

    val toaster: Toaster

    fun inject(vm: ImageListViewModel)
}