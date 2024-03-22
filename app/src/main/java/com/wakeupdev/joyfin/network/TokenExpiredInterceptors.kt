package com.wakeupdev.joyfin.network

import android.util.Log
import com.wakeupdev.joyfin.Const
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody


class TokenExpiredInterceptor(session: SessionInterface) : Interceptor {
    private val TAG = TokenExpiredInterceptor::class.java.simpleName
    private val session: SessionInterface

    init {
        this.session = session
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            //get the response
            val response = chain.proceed(request)
            Log.d(TAG, "intercept got a response code " + response.code)
            when (response.code) {
                403 -> session.onAuthTokenExpired()
                408 -> session.onSessionTimeout()
            }
            response
        } catch (e: Exception) {
            val exceptionMsg = if (e.localizedMessage == null) "" else e.localizedMessage
            session.onSessionTimeout()
            Response.Builder()
                .request(request)
                .code(Const.TOKEN_CODE)
                .protocol(Protocol.HTTP_1_1)
                .body(
                    "server request got timed out"
                        .toResponseBody("application/json".toMediaTypeOrNull())
                )
                .message(exceptionMsg)
                .build()
        }
    }
}
