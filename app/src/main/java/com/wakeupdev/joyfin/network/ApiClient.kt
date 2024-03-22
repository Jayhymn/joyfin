package com.wakeupdev.joyfin.network

import com.google.gson.GsonBuilder
import com.wakeupdev.joyfin.Const
import com.wakeupdev.joyfin.Const.REQUEST_TIME_OUT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private var tokenExpiredListener: OnRequestExceptionListener? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val sessionInterface: SessionInterface by lazy {
        object : SessionInterface {
            override fun onAuthTokenExpired() {
                tokenExpiredListener?.onAuthTokenExpired()
            }

            override fun onSessionTimeout() {
                tokenExpiredListener?.onSessionTimeout()
            }
        }
    }

    fun setAuthTokenExpiredListener(listener: OnRequestExceptionListener?) {
        tokenExpiredListener = listener
    }

    fun clearTokenExpiredListener(listener: OnRequestExceptionListener?) {
        tokenExpiredListener = null
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Const.API_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .create()
                )
            )
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.MINUTES)
            .readTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.MINUTES)
            .writeTimeout(REQUEST_TIME_OUT.toLong(), TimeUnit.MINUTES)
            .addInterceptor(TokenExpiredInterceptor(sessionInterface))
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("accept", "application/json")

                // Add Authorization token if available
                // if (!TextUtils.isEmpty(PrefUtils.getUserToken(context))) {
                //     requestBuilder.addHeader("AuthToken", PrefUtils.getUserToken(context))
                // }

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
    }

    interface OnRequestExceptionListener {
        fun onAuthTokenExpired()
        fun onSessionTimeout()
    }
}
