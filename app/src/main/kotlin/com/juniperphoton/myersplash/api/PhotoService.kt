package com.juniperphoton.myersplash.api

import com.juniperphoton.myersplash.model.SearchResult
import com.juniperphoton.myersplash.model.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoService {
    @GET(Request.PHOTO_URL)
    suspend fun getNewPhotos(
            @Query("page") page: Int,
            @Query("per_page") per_page: Int = Api.DEFAULT_PAGING_INDEX
    ): List<UnsplashImage>

    @GET(Request.SEARCH_URL)
    suspend fun searchPhotos(
            @Query("page") page: Int,
            @Query("per_page") per_page: Int = Api.DEFAULT_PAGING_INDEX,
            @Query("query") query: String
    ): SearchResult
}
