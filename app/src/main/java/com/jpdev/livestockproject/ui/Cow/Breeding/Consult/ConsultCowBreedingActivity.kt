package com.jpdev.livestockproject.ui.Cow.Breeding.Consult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityConsultCowBreedingBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Cow.Consult.Adapter.CowAdapter
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class ConsultCowBreedingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultCowBreedingBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var cowList = mutableListOf<Cattle>()
    private var cowKeys = mutableListOf<String>()
    private lateinit var adapter:CowAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultCowBreedingBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        firebaseInstance = FirebaseInstance(this)

        initListeners(user,farm)
    }

    private fun initListeners(user:String?,farm:String?){
        getListCows(user,farm)
        binding.btnRegisterCow.setOnClickListener {
            val intent = Intent(this, HomeCowActivity::class.java)
            intent.putExtra("userKey",user.toString())
            intent.putExtra("farmKey",farm.toString())
            startActivity(intent)
            finish()
        }
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey",user.toString())
            intent.putExtra("farmKey",farm.toString())
            startActivity(intent)
            finish()
        }
    }

    private fun getListCows(user:String?,farm:String?){
        //Crear funcion para obtener una lista con todas las vacas
        firebaseInstance.getUserCows(user.toString(), farm.toString()) { cows, keys ->
            if (cows != null && keys != null) {
                val breedingCowsIndices = cows.indices.filter { cows[it].type != "Breeding" }
                val filteredCows = cows.filterIndexed { index, _ -> index !in breedingCowsIndices }
                val filteredKeys = keys.filterIndexed { index, _ -> index !in breedingCowsIndices }

                cowList.clear()
                cowList.addAll(filteredCows)

                cowKeys.clear()
                cowKeys.addAll(filteredKeys)

                setUpRecyclerView(user.toString(), farm.toString())
            }
        }
    }

    private fun setUpRecyclerView(user:String?,farm:String?){
        adapter = CowAdapter(cowList,cowKeys,user.toString(),farm.toString())
        binding.rvCows.adapter = adapter
        binding.rvCows.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

}