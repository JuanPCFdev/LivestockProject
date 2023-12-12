package com.jpdev.livestockproject.ui.Cow.Consult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityCowDetailsBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Cow.Consult.Adapter.CowViewHolder
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class CowDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCowDetailsBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCowDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)

        printInfo(user, farmKey, cowKey)

        initListeners(user, farmKey)
    }

    private fun initListeners(user: String?, farmKey: String?) {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
        }
    }

    private fun printInfo(user: String?, farmKey: String?, cowKey: String?) {
        firebaseInstance.getCowDetails(user,farmKey,cowKey){
            if(it != null){
                var details = "Marcación : ${it.marking}\n" +
                        "Edad : ${it.age}\n" +
                        "F-Nacimiento : ${it.birthdate}\n" +
                        "Raza : ${it.breed}\n" +
                        "Genero : ${it.gender}\n" +
                        "Peso : ${it.weight}\n" +
                        "Estado : ${it.state}\n" +
                        "Madre : ${it.motherMark}\n" +
                        "Padre : ${it.fatherMark}\n" +
                        "Costo : ${it.cost}\n"

                binding.cowDetails.text = details

            }
        }
    }

}