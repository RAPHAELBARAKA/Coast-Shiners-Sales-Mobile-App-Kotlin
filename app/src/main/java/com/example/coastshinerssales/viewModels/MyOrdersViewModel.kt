package com.example.coastshinerssales.viewmodel

import androidx.lifecycle.*
import com.example.coastshinerssales.models.response.MyOrdersResponse
import com.example.coastshinerssales.repositories.MyOrdersRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class MyOrdersViewModel(private val myOrdersRepo: MyOrdersRepo) : ViewModel() {

    private val _orders = MutableLiveData<List<MyOrdersResponse>>()
    val orders: LiveData<List<MyOrdersResponse>> get() = _orders

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getMyOrders(email: String) {
        viewModelScope.launch {
            try {
                // Fetch orders from repository
                val response: Response<List<MyOrdersResponse>> = myOrdersRepo.fetchMyOrders(email)

                if (response.isSuccessful) {
                    // If successful, update LiveData with the list of orders
                    _orders.postValue(response.body())
                } else {
                    // If failed, show error message
                    _error.postValue("Failed to fetch orders")
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    // Factory for creating the ViewModel with repository dependency
    class MyOrdersViewModelFactory(private val myOrdersRepo: MyOrdersRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyOrdersViewModel::class.java)) {
                return MyOrdersViewModel(myOrdersRepo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
