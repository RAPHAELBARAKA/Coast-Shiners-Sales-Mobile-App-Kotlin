package com.example.coastshinerssales.viewBindings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.coastshinerssales.databinding.FragmentOrderingBinding
import com.example.coastshinerssales.utils.PREFERENCES

class OrderingFragment : Fragment() {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var quantity: String = "1"  // Quantity stored as String
    private var itemPrice: String = "0.0"  // Item price stored as String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOrderingBinding.inflate(inflater, container, false)

        // Retrieve item details passed as arguments
        val itemName = arguments?.getString("itemName") ?: "Unknown Item"
        itemPrice = arguments?.getString("itemPrice") ?: "0.0"  // Get price as String
        val itemImage = arguments?.getString("itemImage") ?: ""

        // Bind the item details to the views
        binding.itemNameTextView.text = itemName
        binding.itemPriceTextView.text = "Ksh $itemPrice"
        Glide.with(requireContext()).load(itemImage).into(binding.itemImageView)

        // Initialize total price with the default quantity of 1
        binding.quantityTextView.text = quantity
        binding.totalPriceTextView.text = "Total Price: Ksh ${calculateTotalPrice()}"

        // Function to update total price
        fun updateTotalPrice() {
            binding.totalPriceTextView.text = "Total Price: Ksh ${calculateTotalPrice()}"
        }

        // Handle quantity increment
        binding.incrementButton.setOnClickListener {
            quantity = (quantity.toInt() + 1).toString()  // Increment and keep as String
            binding.quantityTextView.text = quantity
            updateTotalPrice()
        }

        // Handle quantity decrement
        binding.decrementButton.setOnClickListener {
            if (quantity.toInt() > 1) {
                quantity = (quantity.toInt() - 1).toString()  // Decrement and keep as String
                binding.quantityTextView.text = quantity
                updateTotalPrice()
            }
        }

        // Handle "Add to Cart" button click
        binding.addToCartButton.setOnClickListener {
            val size = binding.sizeEditText.text.toString()
            val color = binding.colorEditText.text.toString()

            if (size.isBlank() || color.isBlank()) {
                Toast.makeText(requireContext(), "Please enter size and color!", Toast.LENGTH_SHORT).show()
            } else {
                val totalPrice = calculateTotalPrice()  // Calculate total price as String
                pref = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                editor = pref.edit()

                val newItem = "$itemName,$size,$color,$quantity,$totalPrice"
                val existingCart = pref.getString("cart", "") ?: ""
                val updatedCart = if (existingCart.isBlank()) newItem else "$existingCart;$newItem"

                editor.putString("cart", updatedCart).apply()
                Toast.makeText(requireContext(), "Item added to cart!", Toast.LENGTH_SHORT).show()

                // Optionally navigate to CartOrderingFragment
                val intent = Intent(activity, SinglePageActivity::class.java).apply {
                    putExtra("FRAGMENT_TO_BE_SHOWN", "OrderingCartFragment")
                }
                startActivity(intent)
            }
        }

        return binding.root
    }

    // Function to calculate total price
    private fun calculateTotalPrice(): String {
        // Convert quantity and itemPrice from String to Double and multiply them
        val totalPrice = quantity.toDouble() * itemPrice.toDouble()
        return "%.2f".format(totalPrice)  // Return formatted price as String
    }

    // Function to get cart data from SharedPreferences
    private fun getCartData(): MutableList<String> {
        val cartString = pref.getString("cart", "") ?: ""
        val cartData = mutableListOf<String>()
        if (cartString.isNotEmpty()) {
            // Split the string by semicolons to get individual items in the cart
            cartData.addAll(cartString.split(";"))
        }
        return cartData
    }
}
