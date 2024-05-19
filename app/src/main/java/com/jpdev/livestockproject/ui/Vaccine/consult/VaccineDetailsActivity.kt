package com.jpdev.livestockproject.ui.Vaccine.consult

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityVaccineDetailsBinding
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.RvVaccineActivity

class VaccineDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVaccineDetailsBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaccineDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")
        val vaccine = intent.extras?.getString("vaccineKey")

        setData(user, farmKey, cowKey, vaccine)
        initListeners()
    }

    private fun initListeners() {
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
    }

    private fun setData(user: String?, farmKey: String?, cowKey: String?, vaccineKey: String?) {
        var description = ""
        firebaseInstance.getVaccineDetails(user, farmKey, cowKey, vaccineKey) {
            description += "Nombre : ${it.vacineName}\n" +
                    "Costo : ${it.vaccineCost}\n" +
                    "Suministrador : ${it.supplier}\n" +
                    "Fecha : ${it.date}"
            binding.tvDescriptionVaccine.text = description
        }
    }
}