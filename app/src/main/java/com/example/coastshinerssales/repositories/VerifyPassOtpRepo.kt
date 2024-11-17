package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.models.requests.VerifyPassOtpRequest
import com.example.coastshinerssales.models.response.VerifyOtpResponse
import com.example.coastshinerssales.models.response.VerifyPassOtpResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class VerifyPassOtpRepo {
    private val apiService = RetrofitInstance().apiService

    suspend fun verifyPassOtp(verifyPassOtpRequest: VerifyPassOtpRequest): Response<VerifyPassOtpResponse> {

        return apiService.verifypasswordOtp(verifyPassOtpRequest)
    }
}