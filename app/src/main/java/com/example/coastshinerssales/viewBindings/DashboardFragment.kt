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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coastshinerssales.R
import com.example.coastshinerssales.adapters.OrdersAdapter
import com.example.coastshinerssales.databinding.FragmentDashboardBinding
import com.example.coastshinerssales.models.response.PopularItemResponse
import com.example.coastshinerssales.repositories.MyOrdersRepo
import com.example.coastshinerssales.repositories.PopularItemsRepo
import com.example.coastshinerssales.repositories.PopularItemsRepoImp
import com.example.coastshinerssales.viewModels.PopularItemsViewModel
import com.example.coastshinerssales.viewmodel.MyOrdersViewModel
import com.example.coastshinerssales.utils.PREFERENCES

class DashboardFragment : Fragment() {
    private lateinit var viewModel: PopularItemsViewModel
    private lateinit var repository: PopularItemsRepo
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var myOrdersViewModel: MyOrdersViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDashboardBinding.inflate(inflater, container, false)
        binding.viewAllOrdersTextView.setOnClickListener {
            // Navigate to AllOrdersFragment
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view, AllOrdersFragment())
            transaction.addToBackStack(null)  // Optional: Add to back stack if you want to go back
            transaction.commit()
        }
        // Initialize SharedPreferences after inflating the view
        pref = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

        // Initialize repository and ViewModel
        repository = PopularItemsRepoImp()
        val factory = PopularItemsViewModel.PopularItemsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PopularItemsViewModel::class.java)

        // Initialize RecyclerView
        recyclerView = binding.dashboardRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())  // Set layout manager

        // Initialize MyOrdersViewModel
        val myOrdersRepo = MyOrdersRepo()  // Initialize repo, or use dependency injection
        myOrdersViewModel = ViewModelProvider(this, MyOrdersViewModel.MyOrdersViewModelFactory(myOrdersRepo))
            .get(MyOrdersViewModel::class.java)

        // Observe LiveData for popular items
        viewModel.popularItems.observe(viewLifecycleOwner) { items ->
            if (items.isNotEmpty()) {
                updateUI(items, binding)
            } else {
                Log.e("DashboardFragment", "No items found.")
            }
        }

        // Fetch popular items
        viewModel.fetchPopularItems(listOf(1, 2, 3, 4, 5, 6))

        // Fetch the orders using the email stored in SharedPreferences
        val email = pref.getString("email", "")
        if (!email.isNullOrEmpty()) {
            myOrdersViewModel.getMyOrders(email)
        }

        // Observe LiveData for orders
        myOrdersViewModel.orders.observe(viewLifecycleOwner) { orders ->
            ordersAdapter = OrdersAdapter(orders)
            recyclerView.adapter = ordersAdapter
        }

        return binding.root
    }

    private fun updateUI(items: List<PopularItemResponse>, binding: FragmentDashboardBinding) {
        val flattenedItems = items.flatten()
        val baseUrl = "http://192.168.43.88:3000/"

        // ImageViews for the images
        val imageViews = listOf(
            binding.food, binding.house, binding.cloth,
            binding.office, binding.electronics, binding.health
        )

        // TextViews for the names
        val textViews = listOf(
            binding.foodname, binding.housename, binding.clothename,
            binding.officename, binding.electronicname, binding.healthname
        )

        // TextViews for the prices
        val textViews1 = listOf(
            binding.foodprice, binding.houseprice, binding.clotheprice,
            binding.officeprice, binding.electronicprice, binding.healthprice
        )

        // Bind images and names
        imageViews.forEachIndexed { index, imageView ->
            if (index < flattenedItems.size) {
                val item = flattenedItems[index]

                // Load image into ImageView
                val imageUrl = "$baseUrl${item.image}".replace("\\", "/")
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .into(imageView)

                // Bind name to TextView
                textViews[index].text = item.name
                textViews1[index].text = "Ksh:${item.price}"
            }
        }
    }
}
