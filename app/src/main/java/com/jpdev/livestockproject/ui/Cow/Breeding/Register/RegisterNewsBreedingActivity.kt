package com.jpdev.livestockproject.ui.Cow.Breeding.Register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterNewsBreedingBinding
import com.jpdev.livestockproject.domain.model.BreedingPerformance
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterNewsBreedingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterNewsBreedingBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNewsBreedingBinding.inflate(layoutInflater)
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
            val details = getString(R.string.consult_estadis_title) + " ${it.marking}"
            binding.tvTitle.text = details
            binding.etBirthday.setText(formattedDate)
        }
    }

    private fun initListeners(user: String?, farm: String?, cow: String?) {
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
        binding.btnSaveChanges.setOnClickListener {
            saveNews(user, farm, cow)
            saveCow(user, farm, cow)
        }
        binding.etBirthday.setOnClickListener {
            showDatePickerDialog()
        }
        binding.etInsemination.setOnClickListener {
            showDatePickerDialog2()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha de Nacimiento")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etBirthday.setText("$day/$mes/$year")

    }

    private fun showDatePickerDialog2() {
        val datePicker =
            DatePickerFragment { day, month, year -> onDateSelected2(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha")
    }

    private fun onDateSelected2(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etInsemination.setText("$day/$mes/$year")

    }

    private fun saveNews(user: String?, farm: String?, cow: String?) {
        if (validateCredentials()) {
            val New = BreedingPerformance(
                binding.etBirthday.text.toString(),
                binding.etInsemination.text.toString(),
                binding.etWeight.text.toString().toInt(),
                binding.chkIsSick.isChecked,
                binding.chkIsDead.isChecked,
                binding.etDiet.text.toString()
            )
            firebaseInstance.registerNewsBreeding(New, user, farm, cow)
            Toast.makeText(this, "Novedad registrada", Toast.LENGTH_SHORT).show()
            back()
        }else{
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveCow(user: String?, farm: String?, cow: String?){
        if (validateCredentials()) {
            var father = ""
            var type = if(binding.chkIsDead.isChecked){"Muerta"} else{"Propio"}
            firebaseInstance.getCowDetails(user, farm, cow) {
                val Cow = Cattle(
                    0,
                    binding.etMarking.text.toString(),
                    binding.etBirthday.text.toString(),
                    binding.etWeight.text.toString().toInt(),
                    0,
                    binding.etBreed.text.toString(),
                    type,
                    binding.etGender.text.toString(),
                    "corral",
                    it.marking,
                    father,
                    0.0,
                    false
                )

            firebaseInstance.registerCow(Cow, user, farm)}
            finish()
            Toast.makeText(this, "Registro de vaca de vaca de corral exitoso", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Falta por llenar algun dato", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCredentials(): Boolean {
        var validate = false
        if (binding.toString().isNotEmpty()
            && binding.etBirthday.toString().isNotEmpty()
            && binding.etInsemination.toString().isNotEmpty()
            && binding.etWeight.toString().isNotEmpty()
            && binding.etDiet.toString().isNotEmpty()
            && binding.etGender.toString().isNotEmpty()
            && binding.etMarking.toString().isNotEmpty()
            && binding.etBreed.toString().isNotEmpty()
        ) {
            validate = true
        }
        return validate
    }
    
    private fun back() {
        finish()
    }
}