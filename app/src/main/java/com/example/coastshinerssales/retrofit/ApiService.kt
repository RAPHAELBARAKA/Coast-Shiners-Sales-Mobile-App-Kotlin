package com.example.coastshinerssales.retrofit

import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.SignUpRequest
import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.models.response.SignUpResponse
import com.example.coastshinerssales.models.response.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/")
    suspend fun signup(@Body signup: SignUpRequest): Response<SignUpResponse>


    @POST("login")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

    @POST("verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

}