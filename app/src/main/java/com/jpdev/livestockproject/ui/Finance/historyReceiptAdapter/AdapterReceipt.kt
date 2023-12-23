package com.jpdev.livestockproject.ui.Finance.historyReceiptAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Receipt

class AdapterReceipt (private val receiptList: List<Receipt>, val keys: List<String>, val userKey: String, val farmKey: String): RecyclerView.Adapter<ReceiptViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReceiptViewHolder(layoutInflater.inflate(R.layout.item_receipt, parent, false))
    }

    override fun getItemCount() = receiptList.size

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        val item = receiptList[position]
        val key = keys[position]
        holder.bind(item,key, userKey, farmKey)
    }

}