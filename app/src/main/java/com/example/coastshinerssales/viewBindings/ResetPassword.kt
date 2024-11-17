package com.example.coastshinerssales.viewBindings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.coastshinerssales.R
import com.example.coastshinerssales.databinding.ActivityLoginPageBinding
import com.example.coastshinerssales.databinding.ActivityResetPasswordBinding
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.models.requests.ResetPasswordRequest
import com.example.coastshinerssales.repositories.LoginRepo
import com.example.coastshinerssales.repositories.ResetPasswordRepo
import com.example.coastshinerssales.viewModels.LoginViewModel
import com.example.coastshinerssales.viewModels.ResetPasswordViewModel
import kotlinx.coroutines.launch

class ResetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels {
        ResetPasswordViewModel.ResetPasswordViewModelFactory(ResetPasswordRepo())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Observe the login response
        resetPasswordViewModel.resetPasswordResponse.observe(this) { response ->
            binding.loginProgressBar.visibility = View.GONE
            if (response.isSuccessful) {
                val responseBody = response.body()
                responseBody?.let {
                    when (it.message) {
                        "Password updated successfully" -> {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoginPage::class.java) // Replace with your destination activity
                            startActivity(intent)
                            finish()
                        }
                        else -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Handle error responses
                Toast.makeText(this, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle login button click
        binding.btnSubmitLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.loginProgressBar.visibility = View.VISIBLE

            val resetPasswordRequest =ResetPasswordRequest(email = email, newPassword = password)
            lifecycleScope.launch {
                resetPasswordViewModel.resetPassword(resetPasswordRequest)
            }
        }
    }
}