package com.example.coastshinerssales.viewBindings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager  // Add this import
import androidx.recyclerview.widget.RecyclerView
import com.example.coastshinerssales.R
import com.example.coastshinerssales.adapters.AllOrdersAdapter
import com.example.coastshinerssales.adapters.OrdersAdapter
import com.example.coastshinerssales.databinding.FragmentAllOrdersBinding
import com.example.coastshinerssales.repositories.MyOrdersRepo
import com.example.coastshinerssales.utils.PREFERENCES
import com.example.coastshinerssales.viewmodel.MyOrdersViewModel

class AllOrdersFragment : Fragment() {

    private lateinit var ordersAdapter: AllOrdersAdapter
    private lateinit var myOrdersViewModel: MyOrdersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAllOrdersBinding.inflate(inflater, container, false)

        // Initialize ViewModel using ViewModelProvider and custom factory
        val repository = MyOrdersRepo() // Initialize your repository
        val factory = MyOrdersViewModel.MyOrdersViewModelFactory(repository)
        myOrdersViewModel = ViewModelProvider(this, factory).get(MyOrdersViewModel::class.java)

        // Initialize SharedPreferences
        pref = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        // Initialize RecyclerView
        recyclerView = binding.dashboardRecyclerView // Ensure the correct ID for RecyclerView in XML

        // Set the LayoutManager for RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context) // Add the layout manager

        // Fetch the orders using the email stored in SharedPreferences
        val email = pref.getString("email", "")
        if (!email.isNullOrEmpty()) {
            myOrdersViewModel.getMyOrders(email)
        }

        // Observe LiveData for orders
        myOrdersViewModel.orders.observe(viewLifecycleOwner) { orders ->

            Log.d("Orders", "Orders data: $orders")
            val ordersProgressBar = binding.loginProgressBar

            if (orders.isNullOrEmpty()) {
                ordersProgressBar.visibility = View.VISIBLE
            } else {
                ordersProgressBar.visibility = View.GONE
                ordersAdapter = AllOrdersAdapter(orders)
                recyclerView.adapter = ordersAdapter
            }
        }

        // Return the inflated layout with the binding
        return binding.root
    }
}
