package com.jpdev.livestockproject.ui.Cow.Lifting.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class RegisterCowActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterCowBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterCowBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        initListeners(user,farm)
    }

    private fun initListeners(user:String?,farm:String?){
        binding.btnRegisterCow.setOnClickListener {
            if(validateCredentials()){
                val cow = Cattle(
                    0,
                    binding.etMarking.text.toString(),
                    binding.etBirthday.text.toString(),
                    binding.etWeight.text.toString().toInt(),
                    binding.etAge.text.toString().toInt(),
                    binding.etBreed.text.toString(),
                    binding.etState.text.toString(),
                    binding.etGender.text.toString(),
                    "Lifting",
                    "",
                    "",
                    binding.etCost.text.toString().toDouble()
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
            val intent = Intent(this,HomePageActivity::class.java)
            intent.putExtra("userKey",user.toString())
            intent.putExtra("farmKey",farm.toString())
            startActivity(intent)
            finish()
        }

    }

    private fun validateCredentials():Boolean {
        var success = false

        if(binding.etAge.text.toString().isNotEmpty()
            && binding.etBirthday.text.toString().isNotEmpty()
            && binding.etBreed.text.toString().isNotEmpty()
            && binding.etGender.text.toString().isNotEmpty()
            && binding.etState.text.toString().isNotEmpty()
            && binding.etCost.text.toString().isNotEmpty()
            && binding.etMarking.text.toString().isNotEmpty()
            && binding.etWeight.text.toString().isNotEmpty()){
            success = true
        }

        return success
    }

}