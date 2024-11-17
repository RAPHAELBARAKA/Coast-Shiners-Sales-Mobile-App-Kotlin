package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.ResetPasswordRequest
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.models.response.ResetPasswordResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class ResetPasswordRepo {
    private val apiService = RetrofitInstance().apiService

    suspend fun resetpassword(resetpasswordRequest: ResetPasswordRequest): Response<ResetPasswordResponse> {
        return apiService.resetpassword(resetpasswordRequest)
    }
}