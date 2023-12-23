package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityReceiptHistoryBinding
import com.jpdev.livestockproject.domain.model.Receipt
import com.jpdev.livestockproject.ui.Finance.historyReceiptAdapter.AdapterReceipt
import com.jpdev.livestockproject.ui.Finance.sellAdapter.AdapterSell

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
        val receiptKey = intent.extras?.getString("ReceiptKey")
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        getReceipts(key, farmKey)
        binding.btnReturn.setOnClickListener {
            val intent = Intent(this, FinanceActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
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