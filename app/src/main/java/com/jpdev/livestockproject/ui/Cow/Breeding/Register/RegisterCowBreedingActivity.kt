package com.jpdev.livestockproject.ui.Cow.Breeding.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterCowBinding
import com.jpdev.livestockproject.databinding.ActivityRegisterCowBreedingBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class RegisterCowBreedingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterCowBreedingBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterCowBreedingBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        firebaseInstance = FirebaseInstance(this)
        initListener(user, farm)
    }

    private fun initListener(user: String?, farm: String?) {

        binding.btnRegister.setOnClickListener {
            if (validateCredentials()) {
                var mother = ""
                var father = ""
                if(binding.etMother.text.toString().isNotEmpty()){
                    mother = binding.etMother.text.toString()
                }
                if(binding.etFather.text.toString().isNotEmpty()){
                    father = binding.etFather.text.toString()
                }

                val cow = Cattle(
                    0,
                    binding.etMarking.text.toString(),
                    binding.etBirthday.text.toString(),
                    binding.etWeight.text.toString().toInt(),
                    binding.etAge.text.toString().toInt(),
                    binding.etBreed.text.toString(),
                    binding.etState.text.toString(),
                    binding.etGender.text.toString(),
                    "Breeding",
                    mother,
                    father
                )

                firebaseInstance.registerCow(cow,user,farm)

                val intent = Intent(this,HomePageActivity::class.java)
                intent.putExtra("userKey",user.toString())
                intent.putExtra("farmKey",farm.toString())
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Falta por llenar algun dato", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farm)
            startActivity(intent)
            finish()
        }
    }

    private fun validateCredentials(): Boolean {
        var check = false

        if (binding.etMarking.text.toString()
                .isNotEmpty() //Valida si no hay datos vacios (exeptuando madre y padre)
            && binding.etBirthday.text.toString().isNotEmpty()
            && binding.etWeight.text.toString().isNotEmpty()
            && binding.etAge.text.toString().isNotEmpty()
            && binding.etBreed.text.toString().isNotEmpty()
            && binding.etState.text.toString().isNotEmpty()
            && binding.etGender.text.toString().isNotEmpty()
        ) {
            check = true
        } else {
            Toast.makeText(
                this,
                "Hay que llenar todas las casillas obligatorias",
                Toast.LENGTH_SHORT
            ).show()
        }

        return check
    }
}