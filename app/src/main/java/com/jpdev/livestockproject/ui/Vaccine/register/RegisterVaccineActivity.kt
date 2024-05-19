package com.jpdev.livestockproject.ui.Vaccine.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterVaccineBinding
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.RvVaccineActivity

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
        binding.viewToolBar.back.setOnClickListener {
            back()
        }
        binding.tieVaccineDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha de Aplicacion")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.tieVaccineDate.setText("$day/$mes/$year")
    }

    private fun back() {
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
        } else {
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

            firebaseInstance.registerVaccine(vaccine, user, farmKey, cowKey)
            Toast.makeText(this, "Vacuna registrada", Toast.LENGTH_SHORT).show()
            back()
        } else {
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

}