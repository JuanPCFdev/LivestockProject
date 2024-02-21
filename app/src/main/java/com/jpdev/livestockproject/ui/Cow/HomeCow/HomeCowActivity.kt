package com.jpdev.livestockproject.ui.Cow.HomeCow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityHomeCowBinding
import com.jpdev.livestockproject.ui.Cow.Breeding.Consult.ConsultCowBreedingActivity
import com.jpdev.livestockproject.ui.Cow.Corral.CorralActivity
import com.jpdev.livestockproject.ui.Cow.Death.DeathActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.Consult.ConsultCowLiftingActivity
import com.jpdev.livestockproject.ui.Cow.Sold.SoldActivity

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

        binding.btnConsultBredding.setOnClickListener {
            val intent = Intent(this,ConsultCowBreedingActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }

        binding.btnConsultLifting.setOnClickListener {
            val intent = Intent(this,ConsultCowLiftingActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }

        binding.btnConsultCalf.setOnClickListener {
            val intent = Intent(this,CorralActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnDead.setOnClickListener {
            val intent = Intent(this,DeathActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnSold.setOnClickListener {
            val intent = Intent(this,SoldActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
    }
}