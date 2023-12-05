package com.jpdev.livestockproject.ui.Farm.consult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityFarmBinding
import com.jpdev.livestockproject.domain.model.Farm
import com.jpdev.livestockproject.ui.Farm.consult.adapterFarm.adapterFarm
import com.jpdev.livestockproject.ui.Farm.register.FarmRegisterActivity

class FarmActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFarmBinding
    private lateinit var firebaseInstance: FirebaseInstance

    private lateinit var adapter : adapterFarm
    private var farmList = mutableListOf<Farm>()
    private var farmKeys = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        key?.let {
            firebaseInstance.getUserFarms(it) { farms, keys ->
                farms?.let {
                    farmList.clear()
                    farmList.addAll(farms)
                    keys?.let {
                        farmKeys.clear()
                        farmKeys.addAll(keys)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }

        setUpRecyclerView()
        initListeners(key)
    }

    private fun initListeners(key: String?){
        binding.btnRegisterFarm.setOnClickListener{
            val intent = Intent(this, FarmRegisterActivity::class.java)
            intent.putExtra("userKey",key)
            startActivity(intent)
            finish()
        }
    }
    private fun setUpRecyclerView(){
        val key = intent.extras?.getString("userKey")
        adapter = adapterFarm(farmList,farmKeys,key.toString())
        binding.rvFarm.adapter = adapter
        binding.rvFarm.layoutManager = LinearLayoutManager(this)
    }
}