package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityReceiptHistoryBinding
import com.jpdev.livestockproject.domain.model.Receipt
import com.jpdev.livestockproject.ui.Finance.historyReceiptAdapter.AdapterReceipt

class ReceiptHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiptHistoryBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var receiptList = mutableListOf<Receipt>()
    private var receiptKey = mutableListOf<String>()
    private lateinit var adapter: AdapterReceipt
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        getReceipts(key, farmKey)
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
    }
    private fun getReceipts(key: String?, farmKey: String?){
        firebaseInstance.getReceipt(key.toString(),farmKey.toString()){ receipts, strings ->
            if (receipts != null){
                receipts?.let{
                    receiptList.clear()
                    receiptList.addAll(receipts)
                    strings?.let {
                        receiptKey.clear()
                        receiptKey.addAll(strings)
                        setUpRecyclerView(key.toString(), farmKey.toString())
                    }
                }
            }
        }
    }
    private fun setUpRecyclerView(user:String?,farm:String?){
        adapter = AdapterReceipt(receiptList,receiptKey,user.toString(),farm.toString())
        binding.rvReceipt.adapter = adapter
        binding.rvReceipt.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }
}