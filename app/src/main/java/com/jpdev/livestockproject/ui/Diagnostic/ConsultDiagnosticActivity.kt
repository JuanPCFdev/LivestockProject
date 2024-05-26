package com.jpdev.livestockproject.ui.Diagnostic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.data.tips.TipsProvider
import com.jpdev.livestockproject.databinding.ActivityConsultDiagnosticBinding
import com.jpdev.livestockproject.ui.Cow.Consult.Adapter.CowAdapter
import com.jpdev.livestockproject.ui.Diagnostic.Adapter.DiagnosticAdapter
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import com.jpdev.livestockproject.ui.Update.HelpActivity

class ConsultDiagnosticActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultDiagnosticBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var tipsList = TipsProvider()
    private lateinit var adapter: DiagnosticAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultDiagnosticBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        initListeners(user, farm)
    }

    override fun onResume() {
        super.onResume()

        binding = ActivityConsultDiagnosticBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        initListeners(user, farm)
    }

    private fun initListeners(user: String?, farm: String?) {
        setUpRecyclerView(user, farm)
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
    }

    private fun setUpRecyclerView(user: String?, farm: String?) {
        adapter = DiagnosticAdapter(tipsList.getList(), user.toString(), farm.toString())
        binding.rvTips.adapter = adapter
        binding.rvTips.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

}