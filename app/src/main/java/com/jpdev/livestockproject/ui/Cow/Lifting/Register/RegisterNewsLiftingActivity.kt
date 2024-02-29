package com.jpdev.livestockproject.ui.Cow.Lifting.Register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterNewsLiftingBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.LiftingPerformance
import com.jpdev.livestockproject.ui.Cow.Consult.CowDetailsLiftingActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity
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
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        firebaseInstance.getCowDetails(user, farm, cow) {
            val details = getString(R.string.consult_estadis_title) + " ${it.marking}"
            binding.tvTitle.text = details
            binding.etdate.setText(formattedDate)
            binding.etWeight.setText(it.weight.toString())
        }
    }

    private fun initListeners(user: String?, farm: String?, cow: String?) {
        binding.btnHomePage.setOnClickListener {
            goToHome(user, farm)
        }
        binding.btnSaveChanges.setOnClickListener {
            saveNews(user, farm, cow)
        }
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
            val intent = Intent(this, CowDetailsLiftingActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            intent.putExtra("cowKey", cow.toString())
            startActivity(intent)
            finish()

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

    private fun goToHome(user: String?, farmKey: String?) {
        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("userKey", user)
        intent.putExtra("farmKey", farmKey)
        startActivity(intent)
        finish()
    }
}