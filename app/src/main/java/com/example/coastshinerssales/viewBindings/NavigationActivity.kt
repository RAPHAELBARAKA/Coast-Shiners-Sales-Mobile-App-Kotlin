package com.example.coastshinerssales.viewBindings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.coastshinerssales.R
import com.example.coastshinerssales.databinding.ActivityNavigationBinding

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the ImageView to open the drawer
        binding.drawer.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        // Setup navigation drawer
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemClick(menuItem)
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Load initial fragment
        if (savedInstanceState == null) {
            replaceFragment(DashboardFragment())
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun handleNavigationItemClick(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_dashboard -> replaceFragment(DashboardFragment())
            // Add other menu item cases here
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }

    private fun handleLogout() {
        // Clear session data and navigate to login screen
    }
}
