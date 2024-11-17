package com.example.coastshinerssales.viewBindings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.coastshinerssales.R
import com.example.coastshinerssales.databinding.ActivityOtpBinding
import com.example.coastshinerssales.databinding.ActivityOtpPassBinding
import com.example.coastshinerssales.models.requests.ResendOtpRequest
import com.example.coastshinerssales.models.requests.ResendPassOtpRequest
import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.models.requests.VerifyPassOtpRequest
import com.example.coastshinerssales.repositories.ResendOtpRepo
import com.example.coastshinerssales.repositories.ResendPassOtpRepo
import com.example.coastshinerssales.repositories.VerifyOtpRepo
import com.example.coastshinerssales.repositories.VerifyPassOtpRepo
import com.example.coastshinerssales.utils.PREFERENCES
import com.example.coastshinerssales.viewModels.ResendOtpViewModel
import com.example.coastshinerssales.viewModels.ResendPassOtpViewModel
import com.example.coastshinerssales.viewModels.VerifyOtpViewModel
import com.example.coastshinerssales.viewModels.VerifyPassOtpViewModel

class OtpPassActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpPassBinding
    private lateinit var viewModel: VerifyPassOtpViewModel
    private lateinit var pref: SharedPreferences
    private lateinit var resendPassOtpViewModel: ResendPassOtpViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        val email = pref.getString("email", "")
        Log.d("OtpPassActivity", "Retrieved email: $email")

        val otpFields = listOf(
            binding.otp1,
            binding.otp2,
            binding.otp3,
            binding.otp4,

            )

        otpFields.forEachIndexed { index, editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && index < otpFields.size - 1) {
                        otpFields[index + 1].requestFocus()
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
        binding.resendOtp.setOnClickListener {
            if (email != null) {
                if (email.isNotEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                    resendPassOtpViewModel.resendPassOtp(ResendPassOtpRequest(email))
                } else {
                    Toast.makeText(this, "Email not found. Please sign up again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Initialize ViewModel
        resendPassOtpViewModel = ViewModelProvider(
            this, ResendPassOtpViewModel.ResendPassOtpViewModelFactory(ResendPassOtpRepo())
        ).get(ResendPassOtpViewModel::class.java)
        resendPassOtpViewModel.resendPassOtpResponse.observe(this, Observer { response ->
            binding.progressBar.visibility = View.GONE
            if (response.isSuccessful) {
                Toast.makeText(this, "OTP resent successfully. Check your email.", Toast.LENGTH_LONG).show()
            } else {
                showError("Failed to resend OTP: ${response.message()}")
            }
        })

        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            VerifyPassOtpViewModel.VerifyPassOtpViewModelFactory(VerifyPassOtpRepo())
        ).get(VerifyPassOtpViewModel::class.java)

        // Observe OTP verification result
        viewModel.verifyPassOtpResponse.observe(this, Observer { response ->
            binding.progressBar.visibility = View.GONE
            response?.let {
                // Check if the response indicates success
                if (response.isSuccessful) {
                    Toast.makeText(this, "Otp verified successful", Toast.LENGTH_LONG).show()
                    // Navigate to LoginPage if the OTP verification is successful
                    val intent = Intent(this, ResetPassword::class.java)
                    startActivity(intent)
                    finish() // Optionally finish the current activity so that the user cannot go back to the OTP page
                } else {
                    // Handle failure (if any other response type)
                    showError("OTP Verification Failed: ${response.message()}")
                }
            } ?: showError("OTP Verification Failed: No response body")
        })


        // Submit OTP on button click
        binding.btnSubmitOtp.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            // Concatenate OTP fields
            val enteredOTP = otpFields.joinToString("") { it.text.toString() }

            if (enteredOTP.length == 4) {
                // Create the VerifyOtpRequest object with the OTP
                val verifyPassOtpRequest = VerifyPassOtpRequest(enteredOTP)

                // Call the ViewModel method to verify the OTP
                viewModel.verifyPassOtp(verifyPassOtpRequest)
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }


    // Show error message
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
