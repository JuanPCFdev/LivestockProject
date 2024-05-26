package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivitySellCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Finance.sellAdapter.AdapterSell

class SellCowActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySellCowBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var cowList = mutableListOf<Cattle>()
    private var cowKeys = mutableListOf<String>()
    private lateinit var adapter: AdapterSell
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellCowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initComponents(key, farmKey)
    }

    override fun onResume() {
        super.onResume()
        binding = ActivitySellCowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        getListCows(key,farmKey)
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
    }
    private fun getListCows(user:String?,farm:String?){
        firebaseInstance.getUserCows(user.toString(),farm.toString()){ cows, keys ->
            cows?.let {
                cowList.clear()
                cowList.addAll(cows)
                keys?.let {
                    cowKeys.clear()
                    cowKeys.addAll(keys)
                    setUpRecyclerView(user.toString(),farm.toString())
                }
            }
        }
    }
    private fun setUpRecyclerView(user:String?,farm:String?){
        adapter = AdapterSell(cowList,cowKeys,user.toString(),farm.toString())
        binding.rvCows.adapter = adapter
        binding.rvCows.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }
}