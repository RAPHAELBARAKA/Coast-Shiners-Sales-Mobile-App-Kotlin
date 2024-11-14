package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.models.response.VerifyOtpResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class VerifyOtpRepo {
    private val apiService = RetrofitInstance().apiService

    suspend fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): Response<VerifyOtpResponse> {

        return apiService.verifyOtp(verifyOtpRequest)
    }
}