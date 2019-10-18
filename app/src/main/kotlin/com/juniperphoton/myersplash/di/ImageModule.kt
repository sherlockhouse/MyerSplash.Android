package com.juniperphoton.myersplash.di

import com.juniperphoton.myersplash.api.Api
import com.juniperphoton.myersplash.api.CloudService
import com.juniperphoton.myersplash.api.PhotoService
import dagger.Module
import dagger.Provides

/**
 * @author JuniperPhoton @ Zhihu Inc.
 * @since 2019-10-16
 */
@Module
open class ImageModule {
    @Provides
    fun providePhotoService(): PhotoService {
        return Api.create(PhotoService::class.java)
    }
}