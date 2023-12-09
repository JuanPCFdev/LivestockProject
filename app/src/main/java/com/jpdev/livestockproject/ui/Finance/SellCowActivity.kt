package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivitySellCowBinding

class SellCowActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellCowBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellCowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        binding.btnReturnHome.setOnClickListener {
            val intent = Intent(this, FinanceActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
    }
}