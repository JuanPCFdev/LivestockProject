package com.jpdev.livestockproject.ui.Cow.Lifting.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.domain.model.Receipt
import com.jpdev.livestockproject.ui.Cow.Lifting.Consult.ConsultCowLiftingActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class RegisterCowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterCowBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterCowBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        initListeners(user, farm)
    }

    private fun initListeners(user: String?, farm: String?) {

        binding.btnRegisterCow.setOnClickListener {
            if (validateCredentials()) {
                registerReceiptCow(user, farm)

                val intent = Intent(this, ConsultCowLiftingActivity::class.java)
                intent.putExtra("userKey", user.toString())
                intent.putExtra("farmKey", farm.toString())
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Falta por llenar algún dato", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey", user.toString())
            intent.putExtra("farmKey", farm.toString())
            startActivity(intent)
            finish()
        }

        binding.etBirthday.setOnClickListener {
            showDatePickerDialog()
        }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha de Nacimiento")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etBirthday.setText("$day/$mes/$year")

        val birthDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }
        val actualDate = Calendar.getInstance()

        val ageInMonths = calculateAgeInMonths(birthDate, actualDate)

        binding.etAge.setText(ageInMonths.toString())

    }

    private fun calculateAgeInMonths(dateBirth: Calendar, actualDate: Calendar): Int {
        val diffYears = actualDate.get(Calendar.YEAR) - dateBirth.get(Calendar.YEAR)
        val diffMonths = actualDate.get(Calendar.MONTH) - dateBirth.get(Calendar.MONTH)

        return diffYears * 12 + diffMonths
    }

    private fun registerReceiptCow(user: String?, farm: String?) {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val receipt = Receipt(
            0,
            binding.etMarking.text.toString(),
            binding.etCost.text.toString().toDouble(),
            formattedDate,
            "Compra ganado",
            "",
            ""
        )
        val cow = Cattle(
            0,
            binding.etMarking.text.toString(),
            binding.etBirthday.text.toString(),
            binding.etWeight.text.toString().toInt(),
            binding.etAge.text.toString().toInt(),
            binding.etBreed.text.toString(),
            "Propio",
            binding.etGender.text.toString(),
            "Lifting",
            "",
            "",
            binding.etCost.text.toString().toDouble(),
            binding.radioButtonYes.isChecked
        )
        Toast.makeText(this, "Se registro correctamente", Toast.LENGTH_SHORT).show()
        firebaseInstance.registerCowAndReceipt(cow, receipt, user, farm)
    }


    private fun validateCredentials(): Boolean {
        var success = false

        if (binding.etAge.text.toString().isNotEmpty()
            && binding.etBirthday.text.toString().isNotEmpty()
            && binding.etBreed.text.toString().isNotEmpty()
            && binding.etGender.text.toString().isNotEmpty()
            && binding.etCost.text.toString().isNotEmpty()
            && binding.etMarking.text.toString().isNotEmpty()
            && binding.etWeight.text.toString().isNotEmpty()
        ) {
            success = true
        }

        return success
    }


}