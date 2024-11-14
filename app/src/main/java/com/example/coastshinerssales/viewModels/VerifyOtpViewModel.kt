package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.models.response.VerifyOtpResponse
import com.example.coastshinerssales.repositories.LoginRepo
import com.example.coastshinerssales.repositories.VerifyOtpRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class VerifyOtpViewModel (private val repo: VerifyOtpRepo) : ViewModel() {

    private val _verifyOtpResponse = MutableLiveData<Response<VerifyOtpResponse>>()
    val verifyOtpResponse: LiveData<Response<VerifyOtpResponse>> get() = _verifyOtpResponse

    fun verifyOtp(verifyOtpRequest: VerifyOtpRequest) {
        viewModelScope.launch {
            try {
                val response = repo.verifyOtp(verifyOtpRequest)
                if (response.isSuccessful) {
                    _verifyOtpResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("Login", "Error Response: ${response.code()} ${response.message()}")
                    _verifyOtpResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _verifyOtpResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class VerifyOtpViewModelFactory(private val repo: VerifyOtpRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VerifyOtpViewModel::class.java)) {
                return VerifyOtpViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}