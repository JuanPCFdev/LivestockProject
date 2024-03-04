package com.jpdev.livestockproject.ui.Cow.Monta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityMontaBinding
import com.jpdev.livestockproject.databinding.ActivityRegisterCowBinding

class MontaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMontaBinding
    private lateinit var firebaseInstance: FirebaseInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMontaBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")
        
        initListeners(user, farm, cowKey)
    }

    private fun initListeners(user: String?, farm: String?, cowKey: String?) {

    }
}