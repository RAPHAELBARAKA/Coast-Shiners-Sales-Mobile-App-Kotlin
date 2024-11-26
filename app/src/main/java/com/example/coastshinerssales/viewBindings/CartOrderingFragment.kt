package com.example.coastshinerssales.viewBindings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coastshinerssales.R
import com.example.coastshinerssales.adapters.CartItemAdapter
import com.example.coastshinerssales.databinding.FragmentCartOrderingBinding
import com.example.coastshinerssales.utils.PREFERENCES

class CartOrderingFragment : Fragment() {
    private lateinit var binding: FragmentCartOrderingBinding
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var cart = mutableListOf<CartItem>()

    data class CartItem(
        val name: String,
        val size: String,
        val color: String,
        val quantity: String,
        val totalPrice: String
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(size)
            parcel.writeString(color)
            parcel.writeString(quantity)
            parcel.writeString(totalPrice)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<CartItem> {
            override fun createFromParcel(parcel: Parcel): CartItem = CartItem(parcel)
            override fun newArray(size: Int): Array<CartItem?> = arrayOfNulls(size)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartOrderingBinding.inflate(inflater, container, false)
        pref = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        editor = pref.edit() // Initialize the editor here

        // Load and display cart items
        val cartData = loadCartItems()
        if (cartData.isNotEmpty()) {
            cart = cartData
            updateUI()
        } else {
            Toast.makeText(requireContext(), "Cart is empty!", Toast.LENGTH_SHORT).show()
        }

        binding.placeOrder.setOnClickListener {
            val totalAmount = cart.sumOf { it.totalPrice.toDoubleOrNull() ?: 0.0 }

            val intent = Intent(activity, SinglePageActivity::class.java).apply {
                putExtra("FRAGMENT_TO_BE_SHOWN", "PaymentFragment")
                putExtra("totalAmount", totalAmount)
                putParcelableArrayListExtra("cartItems", ArrayList(cart)) // Parcelable cart items
            }

            startActivity(intent)
        }
        return binding.root
    }
    private fun loadCartItems(): MutableList<CartItem> {
        val cartString = pref.getString("cart", "") ?: ""
        val items = mutableListOf<CartItem>()
        if (cartString.isNotEmpty()) {
            cartString.split(";").forEach { itemData ->
                val itemParts = itemData.split(",")
                if (itemParts.size == 5) {
                    val (name, size, color, quantity, totalPrice) = itemParts
                    items.add(
                        CartItem(
                            name = name,
                            size = size,
                            color = color,
                            quantity = quantity,
                            totalPrice = totalPrice
                        )
                    )
                }
            }
        }
        return items
    }

    private fun updateUI() {
        val totalAmount = cart.sumOf {
            it.totalPrice.toDoubleOrNull() ?: 0.0
        }

        // Setup RecyclerView
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCart.adapter = CartItemAdapter(cart) { position ->
            // Remove the item from the list
            cart.removeAt(position)

            // Save updated cart back to SharedPreferences
            saveCartItems()

            // Refresh UI
            updateUI()
        }

        // Update the total amount text
        binding.cartItemTotalPriceTextView.text = "Total: Ksh $totalAmount"
    }

    // Save the updated cart back to SharedPreferences
    private fun saveCartItems() {
        val cartString = cart.joinToString(";") { item ->
            "${item.name},${item.size},${item.color},${item.quantity},${item.totalPrice}"
        }
        editor.putString("cart", cartString).apply()
    }

}

