package com.example.coastshinerssales.repositories

import com.example.coastshinerssales.models.response.PopularItemResponse
import com.example.coastshinerssales.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

interface PopularItemsRepo {
    suspend fun fetchPopularItems(codes: List<Int>): List<PopularItemResponse>
}

class PopularItemsRepoImp : PopularItemsRepo {

    // Directly initializing apiService like in ResendOtpRepo
    private val apiService = RetrofitInstance().apiService

    override suspend fun fetchPopularItems(codes: List<Int>): List<PopularItemResponse> {
        return withContext(Dispatchers.IO) {
            coroutineScope {
                val requests = codes.map { code ->
                    async { apiService.getPopularItems(code) }
                }
                requests.mapNotNull { response ->
                    response.await().body() // Extract only successful responses
                }
            }
        }
    }
}
