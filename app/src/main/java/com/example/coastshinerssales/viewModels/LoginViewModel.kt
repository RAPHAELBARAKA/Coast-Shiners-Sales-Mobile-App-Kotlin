package com.example.coastshinerssales.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.response.LoginResponse
import com.example.coastshinerssales.repositories.LoginRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel (private val repo: LoginRepo) : ViewModel() {

    private val _loginResponse = MutableLiveData<Response<LoginResponse>>()
    val loginResponse: LiveData<Response<LoginResponse>> get() = _loginResponse

    fun login(loginRequest:LoginRequest) {
        viewModelScope.launch {
            try {
                val response = repo.login(loginRequest)
                if (response.isSuccessful) {
                    _loginResponse.postValue(response)
                } else {
                    // Log response details for debugging
                    Log.e("Login", "Error Response: ${response.code()} ${response.message()}")
                    _loginResponse.postValue(response)
                }
            } catch (e: Exception) {
                // Handle error and log the exception
                Log.e("SignUp", "Exception: ${e.localizedMessage}")
                _loginResponse.postValue(Response.error(500, okhttp3.ResponseBody.create(null, "")))
            }
        }
    }

    class LoginViewModelFactory(private val repo: LoginRepo) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}