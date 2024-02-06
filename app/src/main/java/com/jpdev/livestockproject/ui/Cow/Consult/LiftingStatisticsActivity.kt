package com.jpdev.livestockproject.ui.Cow.Consult

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityConsultCowsBinding
import com.jpdev.livestockproject.databinding.ActivityLiftingStatisticsBinding

class LiftingStatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiftingStatisticsBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiftingStatisticsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
    }
}