package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.SignUpRequest
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.models.response.SignUpResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class LoginRepo {
    private val apiService = RetrofitInstance().apiService

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        return apiService.login(loginRequest)
    }
}