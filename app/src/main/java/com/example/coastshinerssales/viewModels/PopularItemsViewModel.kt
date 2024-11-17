package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.response.PopularItemResponse
import com.example.coastshinerssales.repositories.PopularItemsRepo
import kotlinx.coroutines.launch
import retrofit2.Response
class PopularItemsViewModel(private val repo: PopularItemsRepo) : ViewModel() {

    private val _popularItems = MutableLiveData<List<PopularItemResponse>>()
    val popularItems: LiveData<List<PopularItemResponse>> get() = _popularItems

    fun fetchPopularItems(codes: List<Int>) {
        viewModelScope.launch {
            try {
                val items = repo.fetchPopularItems(codes)
                _popularItems.postValue(items) // Post the list to LiveData.
            } catch (e: Exception) {
                Log.e("PopularItemsViewModel", "Error: ${e.localizedMessage}")
                _popularItems.postValue(emptyList()) // Post an empty list in case of an error.
            }
        }
    }

    class PopularItemsViewModelFactory(private val repo: PopularItemsRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PopularItemsViewModel::class.java)) {
                return PopularItemsViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
