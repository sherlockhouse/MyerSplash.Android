package com.juniperphoton.myersplash.api

import android.annotation.SuppressLint
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.juniperphoton.myersplash.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Suppress("DEPRECATION")
object CloudService {
    private const val DEFAULT_TIMEOUT = 10
    const val DEFAULT_REQUEST_COUNT = 10
    const val DEFAULT_HIGHLIGHTS_COUNT = 60

    const val HIGHLIGHTS_DELAY_MS = 200L

    const val DOWNLOAD_TIMEOUT_MS = 30_000L

    val retrofit: Retrofit

    private val builder: OkHttpClient.Builder = OkHttpClient.Builder()

    init {
        if (BuildConfig.DEBUG) {
            val ctx = SSLContext.getInstance("SSL")

            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
            })

            ctx.init(null, trustAllCerts, SecureRandom())

            builder.sslSocketFactory(ctx.socketFactory)
        }

        builder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .addInterceptor(NetInterceptor())

        retrofit = Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(Request.BASE_URL)
                .build()
    }

    inline fun <reified T> createService(): T {
        return retrofit.create(T::class.java)
    }
}
