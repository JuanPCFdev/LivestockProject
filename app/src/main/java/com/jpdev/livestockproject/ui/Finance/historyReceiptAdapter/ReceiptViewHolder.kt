package com.jpdev.livestockproject.ui.Finance.historyReceiptAdapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.jpdev.livestockproject.databinding.ItemReceiptBinding
import com.jpdev.livestockproject.domain.model.Receipt
import com.jpdev.livestockproject.ui.Finance.EditDeleteReceiptActivity

class ReceiptViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private val binding = ItemReceiptBinding.bind(view)

    fun bind(receipt: Receipt, ReceiptKey: String, user: String, farmKey: String){
        val context = binding.cvReceipt.context

        binding.tvName.text = receipt.nameReceipt
        binding.tvType.text = receipt.receiptType

        binding.cvReceipt.setOnClickListener {
            val intent = Intent(context, EditDeleteReceiptActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("ReceiptKey",ReceiptKey)
            context.startActivity(intent)
        }

    }
}