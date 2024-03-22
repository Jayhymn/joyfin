//package com.wakeupdev.joyfin.ui.home
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.wakeupdev.joyfin.R
//
//class SavingsAdapter(private val data: List<SavingsAccount>) :
//    RecyclerView.Adapter<SavingsViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingsViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.smart_saver_balance, parent, false)
//        return SavingsViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: SavingsViewHolder, position: Int) {
//        val item = data[position]
//        val formattedBalance = String.format("%.2f", item.balance) // Format balance with 2 decimals
//
//        holder.imageView.setImageResource( // Set image based on account name (optional)
//            when (item.name) {
//                "Smart Saver" -> R.drawable.gold_coin // Replace with your image resource
//                "Green Saver" -> R.drawable.green_icon // Add icons for other accounts (optional)
//                else -> R.drawable.default_icon // Default icon for unknown types (optional)
//            }
//        )
//        holder.titleTextView.text = item.name
//        holder.amountTextView.text = "₦$formattedBalance"
//        holder.lastDepositTextView.text = "Last deposit: --- -, ----" // Set last deposit text (optional)
//    }
//
//    override fun getItemCount(): Int = data.size
//
//    class SavingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.imageView)
//        val titleTextView: TextView = itemView.findViewById(R.id.title)
//        val amountTextView: TextView = itemView.findViewById(R.id.amountText)
//        val lastDepositTextView: TextView = itemView.findViewById(R.id.last_deposit_text_view)
//    }
//}