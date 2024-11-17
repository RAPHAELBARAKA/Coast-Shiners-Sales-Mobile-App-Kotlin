package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.ResendOtpRequest
import com.example.coastshinerssales.models.response.ResendOtpResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class ResendOtpRepo {

    private val apiService = RetrofitInstance().apiService

    suspend fun resendOtp(resendOtpRequest:ResendOtpRequest): Response<ResendOtpResponse> {
        return apiService.resendOtp(resendOtpRequest)
    }
}
