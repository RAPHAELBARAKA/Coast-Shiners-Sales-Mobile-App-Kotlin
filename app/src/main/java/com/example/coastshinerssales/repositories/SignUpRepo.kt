package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.requests.SignUpRequest
import com.example.coastshinerssales.models.response.SignUpResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class SignUpRepo {
    private val apiService = RetrofitInstance().apiService

    suspend fun signUp(signUpRequest: SignUpRequest): Response<SignUpResponse> {
        return apiService.signup(signUpRequest)
    }
}