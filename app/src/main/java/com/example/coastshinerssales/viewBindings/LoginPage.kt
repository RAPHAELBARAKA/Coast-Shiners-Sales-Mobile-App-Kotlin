package com.example.coastshinerssales.viewBindings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coastshinerssales.databinding.ActivityLoginPageBinding
import com.example.coastshinerssales.models.requests.LoginRequest
import com.example.coastshinerssales.repositories.LoginRepo
import com.example.coastshinerssales.viewModels.LoginViewModel
import kotlinx.coroutines.launch

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding

    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModel.LoginViewModelFactory(LoginRepo())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to sign-up page
        binding.Textbtn1.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
        }

        // Observe the login response
        loginViewModel.loginResponse.observe(this) { response ->
            binding.loginProgressBar.visibility = View.GONE
            if (response.isSuccessful) {
                val responseBody = response.body()
                responseBody?.let {
                    when (it.message) {
                        "Login successful" -> {
                            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, NavigationActivity::class.java) // Replace with your destination activity
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

            val loginRequest = LoginRequest(email = email, password = password)
            lifecycleScope.launch {
                loginViewModel.login(loginRequest)
            }
        }
    }
}
