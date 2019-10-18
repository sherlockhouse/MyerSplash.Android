package com.juniperphoton.myersplash.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.juniperphoton.myersplash.api.Api
import com.juniperphoton.myersplash.api.PhotoService
import com.juniperphoton.myersplash.model.UnsplashImage
import com.juniperphoton.myersplash.utils.LiveDataEvent
import com.juniperphoton.myersplash.utils.Pasteur
import com.juniperphoton.myersplash.utils.Response
import com.juniperphoton.myersplash.utils.liveDataEvent
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 * @author JuniperPhoton @ Zhihu Inc.
 * @since 2019-10-13
 */
open class ImageListViewModel(application: Application) : BaseViewModel(application) {
    companion object {
        private const val TAG = "ImageListViewModel"
    }

    @Inject
    lateinit var photoService: PhotoService

    private val _images = MutableLiveData<MutableList<UnsplashImage>>()
    val images: LiveData<MutableList<UnsplashImage>>
        get() = _images

    private val _refreshing = MutableLiveData<LiveDataEvent<Boolean>>()
    val refreshing: LiveData<LiveDataEvent<Boolean>>
        get() = _refreshing

    private val _loadMoreStatus = MutableLiveData<LiveDataEvent<Response>>()
    val loadMoreStatus: LiveData<LiveDataEvent<Response>>
        get() = _loadMoreStatus

    private var page = Api.DEFAULT_PAGING_INDEX

    private var imageList = mutableListOf<UnsplashImage>()

    fun refresh() = launch {
        _refreshing.value = true.liveDataEvent

        try {
            val list = photoService.getNewPhotos(page)
            imageList.clear()
            imageList.addAll(list)
            _images.value = imageList
        } catch (e: Exception) {
            Pasteur.info(TAG, "error on refreshing: $e")
        } finally {
            _refreshing.value = false.liveDataEvent
        }
    }
}