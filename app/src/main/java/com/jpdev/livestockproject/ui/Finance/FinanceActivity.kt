package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.data.network.FirebaseInstance
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

    private fun initComponents(key: String?, farmKey: String? ){

        binding.historialRecibo.setOnClickListener{
            val intent = Intent(this, ReceiptHistoryActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.compras.setOnClickListener {
            val intent = Intent(this, BuyActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.ventas.setOnClickListener {
            val intent = Intent(this, SellCowActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnReturnHome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
    }
}