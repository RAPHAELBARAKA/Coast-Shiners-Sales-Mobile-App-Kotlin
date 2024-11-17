package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.ResendOtpRequest
import com.example.coastshinerssales.models.requests.ResendPassOtpRequest
import com.example.coastshinerssales.models.response.ResendOtpResponse
import com.example.coastshinerssales.models.response.ResendPassOtpResponse
import com.example.coastshinerssales.repositories.ResendOtpRepo
import com.example.coastshinerssales.repositories.ResendPassOtpRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class ResendPassOtpViewModel (private val repo: ResendPassOtpRepo) : ViewModel() {

    private val _resendPassOtpResponse = MutableLiveData<Response<ResendPassOtpResponse>>()
    val resendPassOtpResponse: LiveData<Response<ResendPassOtpResponse>> get() = _resendPassOtpResponse

    fun resendPassOtp(resendPassOtpRequest: ResendPassOtpRequest) {
        viewModelScope.launch {
            try {
                val response = repo.resendPassOtp(resendPassOtpRequest)
                if (response.isSuccessful) {
                    _resendPassOtpResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("Login", "Error Response: ${response.code()} ${response.message()}")
                    _resendPassOtpResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _resendPassOtpResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class ResendPassOtpViewModelFactory(private val repo: ResendPassOtpRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResendPassOtpViewModel::class.java)) {
                return ResendPassOtpViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}