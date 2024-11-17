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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.coastshinerssales.databinding.ActivitySignUpPageBinding
import com.example.coastshinerssales.models.requests.SignUpRequest
import com.example.coastshinerssales.repositories.SignUpRepo
import com.example.coastshinerssales.utils.PREFERENCES
import com.example.coastshinerssales.viewModels.SignUpViewModel
import kotlinx.coroutines.launch

class SignUpPage : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var email: String? = null



    // Initialize the ViewModel
    private val signUpViewModel: SignUpViewModel by viewModels {
        SignUpViewModel.SignUpViewModelFactory(SignUpRepo())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        editor = pref.edit()



        binding.Textbtn1.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
        // Observe the signup response
        signUpViewModel.signupResponse.observe(this) { response ->
            // Hide loading indicator once we get a response
            binding.loginProgressBar.visibility = View.GONE

            if (response.isSuccessful) {
                val responseBody = response.body()
                when (responseBody?.status) {
                    "exist" -> {
                        // User already exists
                        Toast.makeText(this, "User already exists. Please log in.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Successfully registered
                        Toast.makeText(this, "Sign-up successful. Check your email for OTP.", Toast.LENGTH_LONG).show()
                        // Navigate to OTP verification or next screen
                        editor.putString("email", email)
                        editor.apply() // Don't forget to apply changes
                        Log.d("SignUpEmail", "Retrieved email: $email")


                        val intent = Intent(this, OtpActivity::class.java) // Replace with your destination activity
                        startActivity(intent)
                        finish() // Close the sign-up activity
                    }
                }
            } else {
                // Handle failure due to server error
                Toast.makeText(this, "Sign-up failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                binding.loginProgressBar.visibility = View.GONE

            }
        }

        // Handle the sign-up button click
        binding.btnSubmitSignup.setOnClickListener {
            val name = binding.fullName.text.toString().trim()
            email = binding.email.text.toString().trim()  // Assigning to class-level variable
            val phone = binding.phoneNumber.text.toString().trim()
            val id = binding.idNumber.text.toString().trim()
            val password = binding.password.text.toString()
            val confirm = binding.confirmPassword.text.toString()

            // Validate user input
            if (name.isEmpty() || email!!.isEmpty() || phone.isEmpty() || id.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if passwords match
            if (password != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show loading indicator
            binding.loginProgressBar.visibility = View.VISIBLE

            // Create a SignUpRequest object
            val signUpRequest = SignUpRequest(
                name = name,
                email = email!!,
                phone = phone,
                id = id,
                password = password,
                confirm = confirm
            )

            // Call the signUp function in the ViewModel
            lifecycleScope.launch {
                signUpViewModel.signUp(signUpRequest)
            }
        }
    }
}
