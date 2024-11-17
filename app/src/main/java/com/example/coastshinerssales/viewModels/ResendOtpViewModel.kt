package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.ResendOtpRequest
import com.example.coastshinerssales.models.response.ResendOtpResponse
import com.example.coastshinerssales.repositories.ResendOtpRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class ResendOtpViewModel (private val repo: ResendOtpRepo) : ViewModel() {

    private val _resendOtpResponse = MutableLiveData<Response<ResendOtpResponse>>()
    val resendOtpResponse: LiveData<Response<ResendOtpResponse>> get() = _resendOtpResponse

    fun resendOtp(resendOtpRequest:ResendOtpRequest) {
        viewModelScope.launch {
            try {
                val response = repo.resendOtp(resendOtpRequest)
                if (response.isSuccessful) {
                    _resendOtpResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("Login", "Error Response: ${response.code()} ${response.message()}")
                    _resendOtpResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _resendOtpResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class ResendOtpViewModelFactory(private val repo: ResendOtpRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResendOtpViewModel::class.java)) {
                return ResendOtpViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}