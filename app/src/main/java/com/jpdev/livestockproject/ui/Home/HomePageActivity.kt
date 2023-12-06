package com.jpdev.livestockproject.ui.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityHomePageBinding
import com.jpdev.livestockproject.ui.Cow.Consult.ConsultCowsActivity
import com.jpdev.livestockproject.ui.Farm.deleteEdit.FarmEditDeleteActivity
import com.jpdev.livestockproject.ui.User.Consult.UserActivity


class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")

        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        //Obteniendo los datos de la granja que me estan pasando
        firebaseInstance.getFarmDetails(key.toString(),farmKey.toString()){
            binding.titleHomePage.text = it?.nameFarm
        }

        initListeners(key,farmKey)
    }

    private fun initListeners(key:String?,farmKey:String?) {

        binding.btnConsultFarm.setOnClickListener {
            val intent = Intent(this, FarmEditDeleteActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnConsultUser.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnConsultCow.setOnClickListener {
            val intent = Intent(this, ConsultCowsActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnConsultFinance.setOnClickListener {
            //val intent = Intent(this, FinanceActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnDiagnosis.setOnClickListener {
        //    val intent = Intent(this, DiagnosisActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnConsultVaccine.setOnClickListener {
        //    val intent = Intent(this, VaccineActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnConsultHelp.setOnClickListener {
            // val intent = Intent(this, HelpActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnConsultNotification.setOnClickListener {
            //val intent = Intent(this, NotificationActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }

    }
}