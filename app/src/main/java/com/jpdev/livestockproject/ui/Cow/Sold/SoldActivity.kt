package com.jpdev.livestockproject.ui.Cow.Sold

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivitySoldBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Cow.Consult.Adapter.CowAdapter
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity

class SoldActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySoldBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var cowList = mutableListOf<Cattle>()
    private var cowKeys = mutableListOf<String>()
    private lateinit var adapter: CowAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoldBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        firebaseInstance = FirebaseInstance(this)

        initListeners(user, farm)
    }

    private fun initListeners(user: String?, farm: String?) {
        getListCows(user, farm)
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
    }

    private fun getListCows(user: String?, farm: String?) {
        //Crear funcion para obtener una lista con todas las vacas
        firebaseInstance.getUserCows(user.toString(), farm.toString()) { cows, keys ->
            if (cows != null && keys != null) {
                val deathCowsIndices = cows.indices.filter { cows[it].state != "vendido" }
                val filteredCows = cows.filterIndexed { index, _ -> index !in deathCowsIndices }
                val filteredKeys = keys.filterIndexed { index, _ -> index !in deathCowsIndices }

                cowList.clear()
                cowList.addAll(filteredCows)

                cowKeys.clear()
                cowKeys.addAll(filteredKeys)

                setUpRecyclerView(user.toString(), farm.toString())
            }
        }
    }

    private fun setUpRecyclerView(user: String?, farm: String?) {
        adapter = CowAdapter(cowList, cowKeys, user.toString(), farm.toString())
        binding.rvCows.adapter = adapter
        binding.rvCows.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }
}