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
import com.jpdev.livestockproject.ui.Home.HomePageActivity
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
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        firebaseInstance.getCowDetails(user, farm, cow) {
            val details = getString(R.string.consult_estadis_title) + " ${it.marking}"
            binding.tvTitle.text = details
            binding.etBirthday.setText(formattedDate)
        }
    }

    private fun initListeners(user: String?, farm: String?, cow: String?) {
        binding.btnHomePage.setOnClickListener {
            goToHome(user,farm)
        }
        binding.btnSaveChanges.setOnClickListener {
            saveNews(user, farm, cow)
            saveCow(user, farm, cow)
        }
    }

    private fun saveNews(user: String?, farm: String?, cow: String?) {
        if(validateCredentials()){
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
            goToHome(user, farm)

        }else{
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveCow(user: String?, farm: String?, cow: String?){
        if (validateCredentials()) {
            var mother = ""
            var father = ""
            firebaseInstance.getCowDetails(user, farm, cow) {
                it.marking = mother
                val Cow = Cattle(
                    0,
                    binding.etMarking.text.toString(),
                    binding.etBirthday.text.toString(),
                    binding.etWeight.text.toString().toInt(),
                    0,
                    binding.etBreed.text.toString(),
                    "Propia",
                    binding.etGender.text.toString(),
                    "corral",
                    mother,
                    father
                )

            firebaseInstance.registerCow(Cow, user, farm)}

            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey", user.toString())
            intent.putExtra("farmKey", farm.toString())
            startActivity(intent)
            finish()
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