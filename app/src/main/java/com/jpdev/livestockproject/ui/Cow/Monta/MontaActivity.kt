package com.jpdev.livestockproject.ui.Cow.Monta

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityMontaBinding
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.domain.model.Insemination

class MontaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMontaBinding
    private lateinit var firebaseInstance: FirebaseInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMontaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")
        
        initListeners(user, farm, cowKey)
    }

    private fun initListeners(user: String?, farm: String?, cowKey: String?) {
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
        binding.btnRegister.setOnClickListener {
            registerInsemination(user,farm,cowKey)
        }
        binding.etDateMonta.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha de Aplicacion")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etDateMonta.setText("$day/$mes/$year")
    }
    private fun registerInsemination(
        user: String?,
        farmKey: String?,
        cowKey: String?
    ) {
        if (validateCredentials()) {
            val insemination = Insemination(
                binding.etDateMonta.text.toString(),
                binding.etDescription.text.toString(),
                binding.etEsperma.text.toString()
            )

            firebaseInstance.registerInsemination(insemination,user,farmKey,cowKey)
            Toast.makeText(this, "Inseminacion registrada", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCredentials(): Boolean {
        var validate = false
        if (binding.etDateMonta.toString().isNotEmpty()
            && binding.etDescription.toString().isNotEmpty()
            && binding.etEsperma.toString().isNotEmpty()
        ) {
            validate = true
        }else{
            Toast.makeText(this, "Debe de rellenar todos los datos", Toast.LENGTH_SHORT).show()
        }
        return validate
    }

}