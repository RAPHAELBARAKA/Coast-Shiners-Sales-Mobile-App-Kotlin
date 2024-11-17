package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.models.requests.VerifyPassOtpRequest
import com.example.coastshinerssales.models.response.VerifyOtpResponse
import com.example.coastshinerssales.models.response.VerifyPassOtpResponse
import com.example.coastshinerssales.repositories.VerifyOtpRepo
import com.example.coastshinerssales.repositories.VerifyPassOtpRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class VerifyPassOtpViewModel (private val repo: VerifyPassOtpRepo) : ViewModel() {

    private val _verifyPassOtpResponse = MutableLiveData<Response<VerifyPassOtpResponse>>()
    val verifyPassOtpResponse: LiveData<Response<VerifyPassOtpResponse>> get() = _verifyPassOtpResponse

    fun verifyPassOtp(verifyPassOtpRequest: VerifyPassOtpRequest) {
        viewModelScope.launch {
            try {
                val response = repo.verifyPassOtp(verifyPassOtpRequest)
                if (response.isSuccessful) {
                    _verifyPassOtpResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("Login", "Error Response: ${response.code()} ${response.message()}")
                    _verifyPassOtpResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _verifyPassOtpResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class VerifyPassOtpViewModelFactory(private val repo: VerifyPassOtpRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VerifyPassOtpViewModel::class.java)) {
                return VerifyPassOtpViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}