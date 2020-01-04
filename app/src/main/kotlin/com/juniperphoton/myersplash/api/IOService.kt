package com.juniperphoton.myersplash.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface IOService {
    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): ResponseBody

    @GET
    suspend fun reportDownload(@Url url: String): ResponseBody
}