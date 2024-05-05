package com.jpdev.livestockproject.ui.Diagnostic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.tips.Tips
import com.jpdev.livestockproject.databinding.ActivityDiagnosticDetailsBinding
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class DiagnosticDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiagnosticDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnosticDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val title = intent.extras?.getString("title")
        val description = intent.extras?.getString("description")

        printInfo(title.toString(),description.toString())
        initListeners(user,farmKey)
    }

    private fun initListeners(user: String?, farmKey: String?) {
        binding.viewToolBar.back.setOnClickListener {
            val intent = Intent(this, ConsultDiagnosticActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
            finish()
        }
    }

    private fun printInfo(title:String,description:String) {
        binding.title.text = title
        binding.tipDetails.text = description
    }
}