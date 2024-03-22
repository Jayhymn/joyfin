package com.wakeupdev.joyfin.ui.home

import android.content.Context
import android.graphics.drawable.RotateDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.wakeupdev.joyfin.R
import com.wakeupdev.joyfin.databinding.TransactionItemBinding
import com.wakeupdev.joyfin.models.Transaction
import com.wakeupdev.joyfin.utils.AppStringUtils
import java.util.Date

class TransactionAdapter(
    private val mContext: Context,
    private val transactions: List<Transaction>,
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransactionItemBinding.inflate(
            LayoutInflater.from(mContext),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isCredit = transactions[position].type == "credit"
        val formattedAmount = AppStringUtils.customAmountFormat(transactions[position].amount)

        if (isCredit) {
//            holder.transactionBinding.amount.setTextColor(
//                ResourcesCompat.getColor(mContext.resources, R.color.error, null)
//            )

            holder.transactionBinding.amount.text = "-$formattedAmount"
            holder.transactionBinding.drawableTransaction.background = ResourcesCompat.getDrawable(
                mContext.resources,
                R.drawable.bg_debit_transaction,
                null
            )

        } else {
            holder.transactionBinding.amount.setTextColor(
                ResourcesCompat.getColor(mContext.resources, R.color.green, null)
            )
            holder.transactionBinding.amount.text = "+$formattedAmount"
            holder.transactionBinding.transactionArrow.rotation = 180f

            //         holder.transactionBinding.transactionArrow.drawable.ro
            holder.transactionBinding.drawableTransaction.background = ResourcesCompat.getDrawable(
                mContext.resources,
                R.drawable.bg_credit_transaction,
                null
            )
        }

        holder.transactionBinding.description.text = transactions[position].narration
        holder.transactionBinding.date.text = transactions[position].dateCreated
        holder.transactionBinding.saverType.text = transactions[position].saverType
    }


    inner class ViewHolder(transactionItemBinding: TransactionItemBinding) :
        RecyclerView.ViewHolder(transactionItemBinding.root) {
        var transactionBinding: TransactionItemBinding

        init {
            transactionBinding = transactionItemBinding
        }
    }

    fun filterTransactionsByDate(){
        fun isBetween(date: Date, startDate: Date, endDate: Date): Boolean {
            return date.after(startDate) && date.before(endDate)
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

//    override fun getFilter(): Filter {
//        return itemFilter
//    }


//    init {
//        mAllItems = outletList
//        currentUserLocation = userLocation
//        mFilterItems = outletList
//        mOutletItemListener = outletItemListener
//    }


    companion object {
        private val TAG = TransactionAdapter::class.java.simpleName
    }
}
