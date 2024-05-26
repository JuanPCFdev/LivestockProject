package com.jpdev.livestockproject.ui.Cow.Lifting.EditDelete

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityEditDeleteLiftingBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Cow.Consult.CowResumeActivity
import com.jpdev.livestockproject.ui.Cow.Death.NotifyDeathCow

class EditDeleteLiftingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditDeleteLiftingBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDeleteLiftingBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)

        initListeners(user, farmKey, cowKey)

        printInfo(user, farmKey, cowKey)

    }

    private fun initListeners(user: String?, farmKey: String?, cowKey: String?) {
        binding.btnSaveChanges.setOnClickListener {
            saveChanges(user, farmKey, cowKey)
        }
        binding.btnDeleteCow.setOnClickListener {
            deleteCow(user, farmKey, cowKey)
        }
        binding.btnNotifyDeath.setOnClickListener {
            val intent = Intent(this, NotifyDeathCow::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farmKey)
            intent.putExtra("cowKey", cowKey)
            startActivity(intent)
        }
        binding.viewToolBar.back.setOnClickListener {
            val intent = Intent(this,CowResumeActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("cowKey",cowKey)
            startActivity(intent)
            finish()
        }
    }

    private fun saveChanges(user: String?, farmKey: String?, cowKey: String?) {

        if(validateCredentials()){
            val updatedCow = Cattle(
                0,
                binding.etMarking.text.toString(),
                binding.etBirthday.text.toString(),
                binding.etWeight.text.toString().toInt(),
                binding.etAge.text.toString().toInt(),
                binding.etBreed.text.toString(),
                "Propio",
                binding.etGender.text.toString(),
                "Lifting",
                "",
                "",
                binding.etCost.text.toString().toDouble(),
                binding.radioButtonYes.isChecked
            )

            firebaseInstance.editCow(updatedCow, user, farmKey, cowKey)
            finish()
            Toast.makeText(this, "Se han actualizado los datos", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Debe de llenar todos los campos obligatorios", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateCredentials():Boolean{
        return !(binding.etMarking.text.isNullOrEmpty() ||
                binding.etBirthday.text.isNullOrEmpty() ||
                binding.etWeight.text.isNullOrEmpty() ||
                binding.etAge.text.isNullOrEmpty() ||
                binding.etBreed.text.isNullOrEmpty() ||
                binding.etGender.text.isNullOrEmpty() ||
                binding.etCost.text.isNullOrEmpty() )
    }

    private fun printInfo(user: String?, farmKey: String?, cowKey: String?) {

        firebaseInstance.getCowDetails(user, farmKey, cowKey) {

            binding.etMarking.setText(it.marking)
            binding.etBirthday.setText(it.birthdate)
            binding.etWeight.setText(it.weight.toString())
            binding.etAge.setText(it.age.toString())
            binding.etBreed.setText(it.breed)
            binding.etGender.setText(it.gender)
            binding.etCost.setText(it.cost.toString())
        }
    }

    private fun deleteCow(user: String?, farmKey: String?, cowKey: String?) {
        firebaseInstance.getCowDetails(user, farmKey, cowKey) { cow ->
            val nameReceiptCow = cow.marking

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar vaca")
            builder.setMessage("¿Estás seguro de que quieres eliminar esta vaca?, si se elimina tambien se eliminara el recibo tanto de compra como de venta")

            builder.setPositiveButton("Sí") { _, _ ->
                    // Eliminar la vaca y el recibo
                firebaseInstance.deleteReceiptAndCowByCommonName(user, farmKey, nameReceiptCow)

                finish()

                Toast.makeText(
                    this@EditDeleteLiftingActivity,
                    "Vaca y recibo eliminados exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
            }

            builder.setNegativeButton("No") { _, _ ->
                // No hace nada
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }
}