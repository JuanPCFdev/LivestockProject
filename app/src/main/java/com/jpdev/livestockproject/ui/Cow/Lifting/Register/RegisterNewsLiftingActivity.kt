package com.jpdev.livestockproject.ui.Cow.Lifting.Register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterNewsLiftingBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.domain.model.LiftingPerformance
import com.jpdev.livestockproject.ui.Cow.Consult.CowDetailsLiftingActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterNewsLiftingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterNewsLiftingBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNewsLiftingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cow = intent.extras?.getString("cowKey")

        initListeners(user, farm, cow)
        printInfo(user, farm, cow)
    }

    private fun printInfo(user: String?, farm: String?, cow: String?) {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        firebaseInstance.getCowDetails(user, farm, cow) {
            val details = getString(R.string.register_news) + " ${it.marking}"
            binding.tvTitle.text = details
            binding.etdate.setText(formattedDate)
            binding.etWeight.setText(it.weight.toString())
        }
    }

    private fun initListeners(user: String?, farm: String?, cow: String?) {
        binding.viewToolBar.back.setOnClickListener {
            back()
        }
        binding.btnSaveChanges.setOnClickListener {
            saveNews(user, farm, cow)
        }
        binding.date.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha de Nacimiento")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etdate.setText("$day/$mes/$year")

    }

    private fun saveNews(user: String?, farm: String?, cow: String?) {
        if (validateCredentials()) {
            val New = LiftingPerformance(
                binding.etdate.text.toString(),
                binding.etWeight.text.toString().toInt(),
                binding.etDiet.text.toString()
            )

            firebaseInstance.registerNewsLifting(New, user, farm, cow)
            Toast.makeText(this, "Novedad registrada", Toast.LENGTH_SHORT).show()
            back()

            firebaseInstance.getCowDetails(user, farm, cow) {
                val editCow = Cattle(
                    0,
                    it.marking,
                    it.birthdate,
                    binding.etWeight.text.toString().toInt(),
                    it.age,
                    it.breed,
                    it.state,
                    it.gender,
                    it.type,
                    "",
                    "",
                    it.cost,
                    it.castrated
                )
                firebaseInstance.editCow(editCow, user, farm, cow)
            }

        } else {
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun validateCredentials(): Boolean {
        var validate = false
        if (binding.toString().isNotEmpty()
            && binding.etWeight.toString().isNotEmpty()
            && binding.etDiet.toString().isNotEmpty()
        ) {
            validate = true
        } else {
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
        return validate
    }

    private fun back() {
        finish()
    }
}