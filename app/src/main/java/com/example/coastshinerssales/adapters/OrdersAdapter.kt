package com.example.coastshinerssales.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coastshinerssales.databinding.ItemOrderFileBinding
import com.example.coastshinerssales.models.response.MyOrdersResponse
import com.example.coastshinerssales.models.response.Item
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class OrdersAdapter(private val orders: List<MyOrdersResponse>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()) // ISO 8601 format
    val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Desired format

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

            val orderDateString = order.orderDate // Example: "2024-11-10T15:00:09.711Z"

            val formattedDate = try {
                inputDateFormat.timeZone = TimeZone.getTimeZone("UTC") // Set timezone for ISO 8601
                val date = inputDateFormat.parse(orderDateString) // Parse the input date
                outputDateFormat.format(date) // Format to the desired output
            } catch (e: Exception) {
                e.printStackTrace()
                "Invalid Date" // Fallback value for invalid or null dates
            }
            binding.orderDate.text = formattedDate

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
