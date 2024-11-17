package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.ForgotPasswordRequest
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.response.ForgotPasswordResponse
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class ForgotPasswordRepo {
    private val apiService = RetrofitInstance().apiService

    suspend fun forgotPassowrd(forgotPassowrdRequest: ForgotPasswordRequest): Response<ForgotPasswordResponse> {
        return apiService.forgotPassword(forgotPassowrdRequest)
    }
}