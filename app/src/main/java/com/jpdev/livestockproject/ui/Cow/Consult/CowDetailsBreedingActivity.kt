package com.jpdev.livestockproject.ui.Cow.Consult

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityCowDetailsBreedingBinding
import com.jpdev.livestockproject.domain.model.BreedingPerformance
import com.jpdev.livestockproject.ui.Cow.Breeding.Register.RegisterNewsBreedingActivity
import com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsBreeding.BreedingAdapter

class CowDetailsBreedingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCowDetailsBreedingBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var NewsList = mutableListOf<BreedingPerformance>()
    private var Keys = mutableListOf<String>()
    private lateinit var adapter: BreedingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCowDetailsBreedingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cow = intent.extras?.getString("cowKey")

        initListeners(user, farm, cow)
        getListCowsNews(user, farm, cow)
        printInfo(user, farm, cow)
    }

    override fun onResume() {
        super.onResume()
        binding = ActivityCowDetailsBreedingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cow = intent.extras?.getString("cowKey")

        initListeners(user, farm, cow)
        getListCowsNews(user, farm, cow)
        printInfo(user, farm, cow)
    }

    private fun printDetailsInfo(user: String?, farm: String?, cow: String?) {
        //Cantidad de Partos
        //Cantidad de Partos exitosos
        //Cantidad de Partos Fallidos
        //Cantidad de Crias enfermas
        //Peso promedio de las crias al nacer

        //Cantidad de Partos
        var numberOfBirths = 0
        var numberOfSuccessfullyBirths = 0
        var numberOfFailedBirths = 0
        var numberOfSickBabies = 0
        var avgOfWeight = 0.0
        var aux = 0
        numberOfBirths = NewsList.size
        for (i in NewsList) {

            if (i.PBDeath) {
                numberOfFailedBirths += 1
            }
            if (i.PBSick) {
                numberOfSickBabies += 1
            }
            numberOfSuccessfullyBirths = numberOfBirths - numberOfFailedBirths

            aux += i.PBInitialWeight
        }

        if(numberOfBirths < 1){
            avgOfWeight = 0.0
        }else{
            avgOfWeight = (aux / numberOfBirths).toDouble()
        }

        val textDetails = " ".repeat(8) + "ESTADISTICAS DE RENDIMIENTO.\n" +
                "Cantidad de Partos TOTALES : $numberOfBirths.\n" +
                "Cantidad de Partos EXITOSOS : $numberOfSuccessfullyBirths.\n" +
                "Cantidad de Partos FALLIDOS : $numberOfFailedBirths.\n" +
                "Cantidad de Crias que nacieron enfermas : $numberOfSickBabies.\n" +
                "Peso promedio de las crias al nacer : $avgOfWeight KG."

        binding.tvStats.text = textDetails

    }

    private fun printInfo(user: String?, farm: String?, cow: String?) {
        firebaseInstance.getCowDetails(user, farm, cow) {
            val details = getString(R.string.consult_estadis_title) + " ${it.marking}"
            binding.tvTitle.text = details
        }
    }

    private fun initListeners(user: String?, farm: String?, cow: String?) {
        binding.viewToolBar.back.setOnClickListener {
            val intent = Intent(this,CowResumeActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farm)
            intent.putExtra("cowKey",cow)
            startActivity(intent)
            finish()
        }
        binding.btnRegisterWeight.setOnClickListener {
            val intent = Intent(this, RegisterNewsBreedingActivity::class.java)
            intent.putExtra("userKey", user.toString())
            intent.putExtra("farmKey", farm.toString())
            intent.putExtra("cowKey", cow.toString())
            startActivity(intent)
        }

    }

    private fun getListCowsNews(user: String?, farm: String?, cowKey: String?) {
        firebaseInstance.getCowNewsBreeding(
            user.toString(),
            farm.toString(),
            cowKey.toString()
        ) { news, keys ->
            if (news != null) {
                news?.let {
                    NewsList.clear()
                    NewsList.addAll(news)
                    keys?.let {
                        Keys.clear()
                        Keys.addAll(keys)
                        setUpRecyclerView(user.toString(), farm.toString(), cowKey.toString())
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(user: String?, farm: String?, cowKey: String?) {
        adapter =
            BreedingAdapter(NewsList, Keys, user.toString(), farm.toString(), cowKey.toString())
        binding.rvNews.adapter = adapter
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()

        printDetailsInfo(user, farm, cowKey)
    }
}