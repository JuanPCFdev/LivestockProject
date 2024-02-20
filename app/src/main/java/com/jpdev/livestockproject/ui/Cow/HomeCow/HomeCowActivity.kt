package com.jpdev.livestockproject.ui.Cow.HomeCow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityHomeCowBinding
import com.jpdev.livestockproject.ui.Cow.Breeding.Consult.ConsultCowBreedingActivity
import com.jpdev.livestockproject.ui.Cow.Breeding.Register.RegisterCowBreedingActivity
import com.jpdev.livestockproject.ui.Cow.Consult.LiftingStatisticsActivity
import com.jpdev.livestockproject.ui.Cow.Consult.breedingStatisticsActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.Consult.ConsultCowLiftingActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.Register.RegisterCowActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class HomeCowActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomeCowBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeCowBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")

        initListeners(key,farmKey)
    }

    private fun initListeners(key:String?,farmKey:String?){
        //Home
        binding.btnHomePage.setOnClickListener {
            val intent = Intent(this,HomePageActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        //Statistics Breeding
        binding.btnConsultBreeding.setOnClickListener {
            val intent = Intent(this,breedingStatisticsActivity::class.java) //Estadisticas
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }

        //Statistics lifting
        binding.btnConsultBreeding.setOnClickListener {
            val intent = Intent(this, LiftingStatisticsActivity::class.java) //Estadisticas
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }

        //Consult
        binding.btnConsultBreeding.setOnClickListener {
            val intent = Intent(this,ConsultCowBreedingActivity::class.java) //Consultar Cría
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnConsultLifting.setOnClickListener {
            val intent = Intent(this,ConsultCowLiftingActivity::class.java) //Consultar Levante
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        //Register
        binding.btnRegisterBreeding.setOnClickListener {
            val intent = Intent(this,RegisterCowBreedingActivity::class.java) //Registrar Cría
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnRegisterLifting.setOnClickListener {
            val intent = Intent(this,RegisterCowActivity::class.java) //Registrar Levante
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
    }
}