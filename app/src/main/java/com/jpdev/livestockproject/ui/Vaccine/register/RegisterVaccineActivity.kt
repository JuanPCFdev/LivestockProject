package com.jpdev.livestockproject.ui.Vaccine.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterVaccineBinding
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class RegisterVaccineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterVaccineBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterVaccineBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)

        initListeners(user, farm, cowKey)
    }

    private fun initListeners(user: String?, farmKey: String?, cowKey: String?) {
        binding.btnRegisterVaccine.setOnClickListener {
            registerVaccine(user, farmKey, cowKey)
        }
        binding.btnHomePage.setOnClickListener {
            goToHome(user, farmKey)
        }
    }

    private fun goToHome(user: String?, farmKey: String?) {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("userKey", user)
        intent.putExtra("farmKey", farmKey)
        startActivity(intent)
        finish()
    }

    private fun validateCredentials(): Boolean {
        var validate = false
        if (binding.tieVaccineName.toString().isNotEmpty()
            && binding.tieVaccineDate.toString().isNotEmpty()
            && binding.tieVaccineCost.toString().isNotEmpty()
            && binding.tieVaccineSupplier.toString().isNotEmpty()
        ) {
            validate = true
        }else{
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
        return validate
    }

    private fun registerVaccine(
        user: String?,
        farmKey: String?,
        cowKey: String?
    ) {
        if (validateCredentials()) {
            val vaccine = Vaccine(
                0,
                binding.tieVaccineName.text.toString(),
                binding.tieVaccineCost.text.toString().toInt().toDouble(),
                binding.tieVaccineDate.text.toString(),
                binding.tieVaccineSupplier.text.toString()
                )

            firebaseInstance.registerVaccine(vaccine,user,farmKey,cowKey)
            Toast.makeText(this, "Vacuna registrada", Toast.LENGTH_SHORT).show()
            goToHome(user,farmKey)
        } else {
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

}