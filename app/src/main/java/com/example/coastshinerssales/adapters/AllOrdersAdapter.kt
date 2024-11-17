package com.example.coastshinerssales.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.coastshinerssales.databinding.ItemOrderFileBinding
import com.example.coastshinerssales.models.response.MyOrdersResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AllOrdersAdapter(private val orders: List<MyOrdersResponse>) : RecyclerView.Adapter<AllOrdersAdapter.OrderViewHolder>() {

    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()) // ISO 8601 format
    val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Desired format

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

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
            // StringBuilder to collect item descriptions and quantities
            val itemDescriptions = StringBuilder()
            val itemQuantities = StringBuilder()

            // Loop through the order's items to build the descriptions and quantities
            for (item in order.items) {
                itemDescriptions.append(item.description).append("\n")
                itemQuantities.append(item.quantity).append("\n")
            }

            // Set the text for descriptions and quantities
            binding.itemName.text = itemDescriptions.toString().trim()  // Use trim to remove last newline
            binding.quantity.text = itemQuantities.toString().trim()  // Use trim to remove last newline
        }
    }
}
