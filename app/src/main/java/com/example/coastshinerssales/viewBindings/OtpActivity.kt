package com.example.coastshinerssales.viewBindings

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.coastshinerssales.R
import com.example.coastshinerssales.databinding.ActivityOtpBinding
import com.example.coastshinerssales.models.requests.VerifyOtpRequest
import com.example.coastshinerssales.repositories.VerifyOtpRepo
import com.example.coastshinerssales.viewModels.VerifyOtpViewModel

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var viewModel: VerifyOtpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)



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

        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            VerifyOtpViewModel.VerifyOtpViewModelFactory(VerifyOtpRepo())
        ).get(VerifyOtpViewModel::class.java)

        // Observe OTP verification result
        viewModel.verifyOtpResponse.observe(this, Observer { response ->
            binding.progressBar.visibility = View.GONE
            response?.let {
                // Check if the response indicates success
                if (response.isSuccessful) {
                    Toast.makeText(this, "Otp verified successful", Toast.LENGTH_LONG).show()
                    // Navigate to LoginPage if the OTP verification is successful
                    val intent = Intent(this, LoginPage::class.java)
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
                val verifyOtpRequest = VerifyOtpRequest(enteredOTP)

                // Call the ViewModel method to verify the OTP
                viewModel.verifyOtp(verifyOtpRequest)
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
