package com.example.coastshinerssales.viewBindings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coastshinerssales.R
import com.example.coastshinerssales.databinding.ActivityForgotPasswordBinding
import com.example.coastshinerssales.models.requests.ForgotPasswordRequest
import com.example.coastshinerssales.repositories.ForgotPasswordRepo
import com.example.coastshinerssales.utils.PREFERENCES
import com.example.coastshinerssales.viewModels.ForgotPasswordViewModel
import kotlinx.coroutines.launch

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels {
        ForgotPasswordViewModel.ForgotPasswordViewModelFactory(ForgotPasswordRepo())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        editor = pref.edit()

        // Observe the forgot password response
        forgotPasswordViewModel.forgotPasswordResponse.observe(this) { response ->
            binding.loginProgressBar.visibility = View.GONE
            if (response.isSuccessful) {
                val responseBody = response.body()
                responseBody?.let {
                    when (it.message) {
                        "OTP sent. Check your email for your password OTP." -> {
                            // Show success message
                            Toast.makeText(this, "OTP sent. Check your email for your password OTP.", Toast.LENGTH_SHORT).show()

                            // Save the email to SharedPreferences
                            val email = binding.email.text.toString().trim()
                            if (email.isNotEmpty()) {
                                editor.putString("email", email)
                                editor.apply() // Save the email
                                Log.d("ForgotPassword", "Stored email: $email")
                            } else {
                                Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
                            }

                            // Navigate to the OTP activity
                            val intent = Intent(this, OtpPassActivity::class.java)
                            startActivity(intent)
                            finish() // Optionally finish this activity
                        }
                        else -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Handle error responses
                Toast.makeText(this, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle submit button click
        binding.btnSubmitLogin.setOnClickListener {
            val email = binding.email.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.loginProgressBar.visibility = View.VISIBLE

            // Create the ForgotPasswordRequest object
            val forgotPasswordRequest = ForgotPasswordRequest(email = email)

            // Save the email to SharedPreferences before making the request
            editor.putString("email", email)
            editor.apply()

            // Call the ViewModel method to request forgot password
            lifecycleScope.launch {
                forgotPasswordViewModel.forgotPassword(forgotPasswordRequest)
            }
        }
    }
}
