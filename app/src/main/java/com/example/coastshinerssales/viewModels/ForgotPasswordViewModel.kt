package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.ForgotPasswordRequest
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.response.ForgotPasswordResponse
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.repositories.ForgotPasswordRepo
import com.example.coastshinerssales.repositories.LoginRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class ForgotPasswordViewModel  (private val repo: ForgotPasswordRepo) : ViewModel() {

    private val _forgotPasswordResponse = MutableLiveData<Response<ForgotPasswordResponse>>()
    val forgotPasswordResponse: LiveData<Response<ForgotPasswordResponse>> get() = _forgotPasswordResponse

    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) {
        viewModelScope.launch {
            try {
                val response = repo.forgotPassowrd(forgotPasswordRequest)
                if (response.isSuccessful) {
                    _forgotPasswordResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("Login", "Error Response: ${response.code()} ${response.message()}")
                    _forgotPasswordResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _forgotPasswordResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class ForgotPasswordViewModelFactory(private val repo: ForgotPasswordRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java)) {
                return ForgotPasswordViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}