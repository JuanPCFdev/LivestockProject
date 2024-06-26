package com.jpdev.livestockproject.ui.Vaccine.deleteEdit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityEditDeleteVaccineBinding
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.RvVaccineActivity

class EditDeleteVaccineActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditDeleteVaccineBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDeleteVaccineBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")
        val vaccineKey = intent.extras?.getString("vaccineKey")

        firebaseInstance = FirebaseInstance(this)

        initListeners(user, farmKey, cowKey, vaccineKey)

    }

    private fun initListeners(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        vaccineKey: String?
    ) {
        setData(user, farmKey, cowKey, vaccineKey)
        binding.btnSave.setOnClickListener {
            saveChanges(user, farmKey, cowKey, vaccineKey)
        }
        binding.btnDelete.setOnClickListener {
            deleteVaccine(user, farmKey, cowKey, vaccineKey)
        }
        binding.viewToolBar.back.setOnClickListener {
            back()
        }
    }

    private fun setData(user: String?, farmKey: String?, cowKey: String?, vaccineKey: String?) {
        firebaseInstance.getVaccineDetails(user, farmKey, cowKey, vaccineKey) {
            binding.tieVaccineName.setText(it.vacineName)
            binding.tieVaccineCost.setText(it.vaccineCost.toString())
            binding.tieVaccineDate.setText(it.date)
            binding.tieVaccineSupplier.setText(it.supplier)
        }
    }

    private fun validateCredentials():Boolean{
        return !(binding.tieVaccineName.text.isNullOrEmpty() ||
                binding.tieVaccineCost.text.isNullOrEmpty() ||
                binding.tieVaccineDate.text.isNullOrEmpty() ||
                binding.tieVaccineSupplier.text.isNullOrEmpty())
    }

    private fun saveChanges(user: String?, farmKey: String?, cowKey: String?, vaccineKey: String?) {

        if(validateCredentials()){
            val updatedVaccine = Vaccine(
                0,
                binding.tieVaccineName.text.toString(),
                binding.tieVaccineCost.text.toString().toDouble(),
                binding.tieVaccineDate.text.toString(),
                binding.tieVaccineSupplier.text.toString()
            )

            firebaseInstance.editVaccine(updatedVaccine, user, farmKey, cowKey, vaccineKey)
            Toast.makeText(this, "Se han actualizado los datos", Toast.LENGTH_SHORT).show()
            back()
        }else{
            Toast.makeText(this, "Debe de llenar los campos obligatorios", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deleteVaccine(
        user: String?,
        farmKey: String?,
        cowKey: String?,
        vaccineKey: String?
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ELIMINAR VACUNA")
        builder.setMessage("¿Estás seguro de que quieres eliminar esta vacuna?")

        builder.setPositiveButton("Sí") { _, _ ->
            firebaseInstance.deleteVaccine(user, farmKey, cowKey, vaccineKey)

            back()

            Toast.makeText(
                this@EditDeleteVaccineActivity,
                "Vacuna eliminada exitosamente",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun back() {
        finish()
    }
}