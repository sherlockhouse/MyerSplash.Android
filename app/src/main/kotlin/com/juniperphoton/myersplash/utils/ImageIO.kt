package com.juniperphoton.myersplash.utils

import android.net.Uri
import com.facebook.drawee.backends.pipeline.Fresco

object ImageIO {
    private const val TAG = "ImageIO"

    fun isInMemoryCache(url: String?): Boolean {
        url ?: return false
        return Fresco.getImagePipeline().isInBitmapMemoryCache(Uri.parse(url))
    }

    fun isInDiskCache(url: String?): Boolean {
        url ?: return false
        return Fresco.getImagePipeline().isInDiskCacheSync(Uri.parse(url))
    }

    fun isInCache(url: String?): Boolean {
        url ?: return false

        val memoryCached = isInMemoryCache(url)
        val diskCached = isInDiskCache(url)

        Pasteur.info(TAG) {
            "in memory cache: $memoryCached for $url"
        }

        Pasteur.info(TAG) {
            "in disk cache: $diskCached for $url"
        }

        return memoryCached || diskCached
    }

    fun clearCache() {
        Fresco.getImagePipeline().clearCaches()
    }
}