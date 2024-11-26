package com.example.coastshinerssales.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.coastshinerssales.R
import com.example.coastshinerssales.databinding.ItemCartBinding
import com.example.coastshinerssales.viewBindings.CartOrderingFragment

class CartItemAdapter(
    private val cartItems: MutableList<CartOrderingFragment.CartItem>,
    private val onItemRemoved: (Int) -> Unit // Callback for item removal
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    class CartItemViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.binding.cartItem = cartItem

        // Handle remove button click
        holder.binding.remove.setOnClickListener {
            onItemRemoved(position) // Trigger callback
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }
}
