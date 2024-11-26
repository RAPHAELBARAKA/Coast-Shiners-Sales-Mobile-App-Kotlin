package com.example.coastshinerssales.viewBindings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.coastshinerssales.databinding.FragmentPaymentBinding
import com.example.coastshinerssales.viewBindings.CartOrderingFragment.CartItem

class PaymentFragment : Fragment() {

    private var totalAmount: Double = 0.0
    private var cartItems: ArrayList<CartItem> = arrayListOf()
    private lateinit var binding: FragmentPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize View Binding
        binding = FragmentPaymentBinding.inflate(inflater, container, false)

        // Retrieve data from Intent
        arguments?.let {
            totalAmount = it.getDouble("totalAmount", 0.0)
            cartItems = it.getParcelableArrayList("cartItems") ?: arrayListOf()
        }

        // Update total amount text
        binding.totalAmountTextView.text = "Total: Ksh $totalAmount"

        // Dynamically add cart items to the layout
        displayCartItems()

        return binding.root
    }

    private fun displayCartItems() {
        val cartItemsLayout: LinearLayout = binding.cartItemsLayout
        cartItemsLayout.removeAllViews()  // Clear any existing views

        if (cartItems.isNotEmpty()) {
            // Loop through each cart item and create a TextView for it
            cartItems.forEach { item ->
                val itemTextView = TextView(requireContext()).apply {
                    text = "Item: ${item.name}, Size: ${item.size}, Color: ${item.color}, Quantity: ${item.quantity}, Price: Ksh ${item.totalPrice}"
                    textSize = 16f
                    setPadding(0, 16, 0, 16)
                }
                cartItemsLayout.addView(itemTextView)
            }
        } else {
            val noItemsTextView = TextView(requireContext()).apply {
                text = "No items in the cart."
                textSize = 16f
                setPadding(0, 16, 0, 16)
            }
            cartItemsLayout.addView(noItemsTextView)
        }
    }
}
