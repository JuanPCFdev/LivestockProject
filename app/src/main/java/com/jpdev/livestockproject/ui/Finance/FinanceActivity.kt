package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.databinding.ActivityFinanceBinding
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class FinanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        binding.btnHistoryReceipt.setOnClickListener {
            val intent = Intent(this, ReceiptHistoryActivity::class.java)
            intent.putExtra("userKey", key)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
        }
        binding.btnBuy.setOnClickListener {
            val intent = Intent(this, BuyActivity::class.java)
            intent.putExtra("userKey", key)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
        }
        binding.btnSold.setOnClickListener {
            val intent = Intent(this, SellCowActivity::class.java)
            intent.putExtra("userKey", key)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
        }
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
        binding.btnEarningsLost.setOnClickListener {
            val intent = Intent(this, EarningLostReceiptActivity::class.java)
            intent.putExtra("userKey", key)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
        }
    }
}