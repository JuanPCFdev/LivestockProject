package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityReceiptHistoryBinding

class ReceiptHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceiptHistoryBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        binding.btnReturn.setOnClickListener {
            val intent = Intent(this, FinanceActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
    }
}