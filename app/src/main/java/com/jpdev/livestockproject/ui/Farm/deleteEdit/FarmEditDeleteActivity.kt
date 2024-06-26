package com.jpdev.livestockproject.ui.Farm.deleteEdit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityFarmEditDeleteBinding
import com.jpdev.livestockproject.domain.model.Farm
import com.jpdev.livestockproject.ui.Farm.consult.FarmActivity
import com.jpdev.livestockproject.ui.Farm.register.FarmRegisterActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class FarmEditDeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFarmEditDeleteBinding
    private lateinit var firebaseInstance: FirebaseInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFarmEditDeleteBinding.inflate(layoutInflater)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        if (key != null && farmKey != null) {
            initListeners(key, farmKey)
            showInformation(key, farmKey)
        }
    }
    private fun initListeners(key:String, farmKey: String){

        binding.btnEdit.setOnClickListener {
            editFarm(key, farmKey)
        }

        binding.btnDelete.setOnClickListener{
            deleteFarm(key, farmKey)
        }
        binding.viewToolBar.back.setOnClickListener{
            finish()
        }
    }
    private fun showInformation(key: String, farmKey: String) {
        val etFarmName = binding.etFarmName
        val etFarmHectares = binding.etFarmHectares
        val etFarmNumCows = binding.etFarmCapacity
        val etFarmAddress = binding.etFarmAddres

        firebaseInstance.getUserFarms(key) { farms, keys ->
            farms?.let {
                // Busca la finca directamente por farmKey
                val farmIndex = keys?.indexOf(farmKey)
                if (farmIndex != -1) {
                    val farm = farms[farmIndex!!]
                    etFarmName.setText(farm.nameFarm)
                    etFarmHectares.setText(farm.hectares.toString())
                    etFarmNumCows.setText(farm.numCows.toString())
                    etFarmAddress.setText(farm.address)
                }
            }
        }
    }

    private fun deleteFarm(key: String, farmKey: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Finca")
        builder.setMessage("¿Estás seguro de que quieres eliminar esta finca?")

        builder.setPositiveButton("Sí") { _, _ ->
            firebaseInstance.deleteFarm(key, farmKey)
            val intent = Intent(this, FarmActivity::class.java)
            intent.putExtra("userKey",key)
            startActivity(intent)
            finish()
            Toast.makeText(
                this@FarmEditDeleteActivity,
                "Finca eliminada exitosamente",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            // No hace nada
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
    private fun editFarm(key: String, farmKey: String) {
        val etFarmName = binding.etFarmName
        val etFarmHectares = binding.etFarmHectares
        val etFarmNumCows = binding.etFarmCapacity
        val etFarmAddress = binding.etFarmAddres

        firebaseInstance.getUserFarms(key) { farms, keys ->
            farms?.let {
                val farmIndex = keys?.indexOf(farmKey)
                if (farmIndex != -1) {
                    val currentFarm = farms[farmIndex!!]

                    val updatedFarm = Farm(
                        etFarmName.text.toString(),
                        etFarmHectares.text.toString().toDouble(),
                        etFarmNumCows.text.toString().toInt(),
                        etFarmAddress.text.toString()
                    )

                    if (currentFarm != updatedFarm) {
                        // Solo realiza la edición si hay cambios
                        firebaseInstance.editFarm(updatedFarm, key, farmKey)

                        Toast.makeText(
                            this@FarmEditDeleteActivity,
                            "Información de la finca actualizada exitosamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@FarmEditDeleteActivity,
                            "No se realizaron cambios en la información de la finca",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }
    }

}