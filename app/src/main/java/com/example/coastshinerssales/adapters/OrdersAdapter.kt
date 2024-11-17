package com.example.coastshinerssales.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coastshinerssales.databinding.ItemOrderFileBinding
import com.example.coastshinerssales.models.response.MyOrdersResponse
import com.example.coastshinerssales.models.response.Item

class OrdersAdapter(private val orders: List<MyOrdersResponse>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    private var isExpanded = false  // Flag to track if all orders should be shown

    // Toggle between showing only two orders or all orders
    fun toggleView() {
        isExpanded = !isExpanded
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = if (isExpanded || position < 2) orders[position] else return // Show only first two orders initially
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return if (isExpanded) orders.size else 2 // Show only two orders initially
    }

    inner class OrderViewHolder(private val binding: ItemOrderFileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: MyOrdersResponse) {
            binding.orderNumber.text = order.orderNumber
            binding.status.text = order.status
            binding.orderDate.text = order.orderDate

            // Collect item descriptions and quantities
            val itemDescriptions = StringBuilder()
            val itemQuantities = StringBuilder()

            for (item in order.items) {
                itemDescriptions.append(item.description).append("\n")
                itemQuantities.append(item.quantity).append("\n")
            }

            binding.itemName.text = itemDescriptions.toString()
            binding.quantity.text = itemQuantities.toString()
        }
    }
}
