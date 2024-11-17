package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.ResendOtpRequest
import com.example.coastshinerssales.models.requests.ResendPassOtpRequest
import com.example.coastshinerssales.models.response.ResendOtpResponse
import com.example.coastshinerssales.models.response.ResendPassOtpResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class ResendPassOtpRepo {

    private val apiService = RetrofitInstance().apiService

    suspend fun resendPassOtp(resendPassOtpRequest: ResendPassOtpRequest): Response<ResendPassOtpResponse> {
        return apiService.resendPassOtp(resendPassOtpRequest)
    }
}