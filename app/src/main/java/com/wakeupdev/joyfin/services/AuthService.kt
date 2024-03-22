package com.wakeupdev.joyfin.services

import com.wakeupdev.joyfin.models.LoginCredential
import com.wakeupdev.joyfin.models.networkres.ApiResponse
import com.wakeupdev.joyfin.models.networkres.UserData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthService {
    @GET("main/mock.json")
    fun loginUser(): Call<ApiResponse?>
}