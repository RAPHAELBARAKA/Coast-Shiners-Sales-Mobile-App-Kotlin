package com.example.coastshinerssales.retrofit

import com.example.coastshinerssales.models.requests.ForgotPasswordRequest
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.ResendOtpRequest
import com.example.coastshinerssales.models.requests.ResendPassOtpRequest
import com.example.coastshinerssales.models.requests.ResetPasswordRequest
import com.example.coastshinerssales.models.requests.SignUpRequest
import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.models.requests.VerifyPassOtpRequest
import com.example.coastshinerssales.models.response.ForgotPasswordResponse
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.models.response.MyOrdersResponse
import com.example.coastshinerssales.models.response.PopularItemResponse
import com.example.coastshinerssales.models.response.ResendOtpResponse
import com.example.coastshinerssales.models.response.ResendPassOtpResponse
import com.example.coastshinerssales.models.response.ResetPasswordResponse
import com.example.coastshinerssales.models.response.SignUpResponse
import com.example.coastshinerssales.models.response.VerifyOtpResponse
import com.example.coastshinerssales.models.response.VerifyPassOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/")
    suspend fun signup(@Body signup: SignUpRequest): Response<SignUpResponse>


    @POST("login")
    suspend fun login(@Body login: LoginRequest): Response<LoginResponse>

    @POST("verify-otp")
    suspend fun verifyOtp(@Body verifyOtp: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("password-otp")
    suspend fun forgotPassword(@Body forgotPassword: ForgotPasswordRequest): Response<ForgotPasswordResponse>


    @POST("verifypassword-otp")
    suspend fun verifypasswordOtp(@Body verifypasswordOtp: VerifyPassOtpRequest): Response<VerifyPassOtpResponse>

    @POST("resetpassword")
    suspend fun resetpassword(@Body resetpassword: ResetPasswordRequest): Response<ResetPasswordResponse>


    @POST("resend-otp")
    suspend fun  resendOtp(@Body resendOtp: ResendOtpRequest): Response<ResendOtpResponse>

    @POST("resendpass-otp")
    suspend fun  resendPassOtp(@Body resendPassOtp: ResendPassOtpRequest): Response<ResendPassOtpResponse>

    @GET("get-item")
    suspend fun getPopularItems(@Query("code") code: Int): Response<PopularItemResponse>

    @GET("api/orders")
    suspend fun getMyOrders(@Query("email") email: String): Response<List<MyOrdersResponse>>

}