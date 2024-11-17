package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.ResetPasswordRequest
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.models.response.ResetPasswordResponse
import com.example.coastshinerssales.repositories.LoginRepo
import com.example.coastshinerssales.repositories.ResetPasswordRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class ResetPasswordViewModel (private val repo: ResetPasswordRepo) : ViewModel() {

    private val _resetPasswordResponse = MutableLiveData<Response<ResetPasswordResponse>>()
    val resetPasswordResponse: LiveData<Response<ResetPasswordResponse>> get() = _resetPasswordResponse

    fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        viewModelScope.launch {
            try {
                val response = repo.resetpassword(resetPasswordRequest)
                if (response.isSuccessful) {
                    _resetPasswordResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("Login", "Error Response: ${response.code()} ${response.message()}")
                    _resetPasswordResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _resetPasswordResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class ResetPasswordViewModelFactory(private val repo: ResetPasswordRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResetPasswordViewModel::class.java)) {
                return ResetPasswordViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}