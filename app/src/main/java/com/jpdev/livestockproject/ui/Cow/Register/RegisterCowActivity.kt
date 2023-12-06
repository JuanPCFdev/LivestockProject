package com.jpdev.livestockproject.ui.Cow.Register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")

        initListeners(user,farm)
    }

    private fun initListeners(user:String?,farm:String?){
        binding.btnRegisterCow.setOnClickListener {
            val cow = Cattle(
                0,
                binding.etMarking.text.toString(),
                binding.etBirthday.text.toString(),
                binding.etWeight.text.toString().toInt(),
                binding.etAge.text.toString().toInt(),
                binding.etBreed.text.toString(),
                binding.etState.text.toString(),
                binding.etGender.text.toString()
            )

            firebaseInstance.registerCow(cow,user,farm)

            val intent = Intent(this,HomePageActivity::class.java)
            intent.putExtra("userKey",user.toString())
            intent.putExtra("farmKey",farm.toString())
            startActivity(intent)
            finish()
        }

        binding.btnHome.setOnClickListener {
            val intent = Intent(this,HomePageActivity::class.java)
            intent.putExtra("userKey",user.toString())
            intent.putExtra("farmKey",farm.toString())
            startActivity(intent)
            finish()
        }

    }

}