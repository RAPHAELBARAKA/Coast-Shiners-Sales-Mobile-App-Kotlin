package com.example.coastshinerssales.viewBindings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.coastshinerssales.databinding.ActivityMainBinding

const val SPLASH_DELAY: Long = 2000


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@MainActivity, LoginPage::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}