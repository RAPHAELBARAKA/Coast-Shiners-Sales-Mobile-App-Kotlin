package com.example.coastshinerssales.viewBindings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.coastshinerssales.R
import com.example.coastshinerssales.databinding.ActivitySinglePageBinding

class SinglePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySinglePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load initial fragment
        if (savedInstanceState == null) {
            // Default fragment can be DashboardFragment or any fragment you want to start with
            replaceFragment(DashboardFragment())
        }

        // Set up bottom navigation listener
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            handleNavigationItemClick(menuItem)
            true
        }

        // Check if any fragment is to be shown from the intent
        val fragmentToBeShown = intent.getStringExtra("FRAGMENT_TO_BE_SHOWN")
        if (fragmentToBeShown == "OrderingCartFragment") {
            replaceFragment(CartOrderingFragment()) // Replace with the specific fragment
        }
        if (fragmentToBeShown == "PaymentFragment") {
            replaceFragment(PaymentFragment()) // Replace with the specific fragment
        }
    }

    private fun handleNavigationItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_dashboard -> replaceFragment(DashboardFragment())
            // Add other cases for additional menu items
            // R.id.nav_other -> replaceFragment(OtherFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    private fun handleLogout() {
        // Handle logout functionality if needed
    }
}
