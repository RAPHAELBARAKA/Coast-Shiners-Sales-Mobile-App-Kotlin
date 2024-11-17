package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.response.MyOrdersResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import retrofit2.Response

class MyOrdersRepo {

    private val apiService = RetrofitInstance().apiService

    suspend fun fetchMyOrders(email: String): Response<List<MyOrdersResponse>> {
        return apiService.getMyOrders(email)  // Make the API call
    }
}
