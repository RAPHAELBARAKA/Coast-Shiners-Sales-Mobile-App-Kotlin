package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.SignUpRequest
import com.example.coastshinerssales.models.response.SignUpResponse
import com.example.coastshinerssales.repositories.SignUpRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel(private val repo: SignUpRepo) : ViewModel() {

    private val _signupResponse = MutableLiveData<Response<SignUpResponse>>()
    val signupResponse: LiveData<Response<SignUpResponse>> get() = _signupResponse

    fun signUp(signupRequest: SignUpRequest) {
        viewModelScope.launch {
            try {
                val response = repo.signUp(signupRequest)
                if (response.isSuccessful) {
                    _signupResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("SignUp", "Error Response: ${response.code()} ${response.message()}")
                    _signupResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _signupResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class SignUpViewModelFactory(private val repo: SignUpRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                return SignUpViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}