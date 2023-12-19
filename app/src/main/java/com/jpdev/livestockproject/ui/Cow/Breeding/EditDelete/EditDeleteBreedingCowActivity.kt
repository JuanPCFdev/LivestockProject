package com.jpdev.livestockproject.ui.Cow.Breeding.EditDelete

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityEditDeleteBreedingCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import com.jpdev.livestockproject.ui.User.LogIn.LogInActivity

class EditDeleteBreedingCowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditDeleteBreedingCowBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDeleteBreedingCowBinding.inflate(layoutInflater)
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
        binding.btnDelete.setOnClickListener {
            deleteCow(user,farmKey,cowKey)
        }
        binding.btnSaveChanges.setOnClickListener {
            saveChanges(user,farmKey,cowKey)
        }
    }

    private fun saveChanges(user: String?, farmKey: String?, cowKey: String?){
        val updatedCow = Cattle(
            0,
            binding.etMarking.text.toString(),
            binding.etBirthday.text.toString(),
            binding.etWeight.text.toString().toInt(),
            binding.etAge.text.toString().toInt(),
            binding.etBreed.text.toString(),
            binding.etState.text.toString(),
            binding.etGender.text.toString(),
            "Breeding",
            binding.etMother.text.toString(),
            binding.etFather.text.toString(),
            0.0
        )

        firebaseInstance.editCow(updatedCow,user,farmKey,cowKey)

        Toast.makeText(this,"Se han actualizado los datos",Toast.LENGTH_SHORT).show()

        val intent = Intent(this, HomePageActivity::class.java)
        intent.putExtra("userKey",user)
        intent.putExtra("farmKey",farmKey)
        startActivity(intent)
        finish()
    }

    private fun printInfo(user: String?, farmKey: String?, cowKey: String?) {
        firebaseInstance.getCowDetails(user, farmKey, cowKey) {

            binding.etMarking.setText(it.marking)
            binding.etBirthday.setText(it.birthdate)
            binding.etWeight.setText(it.weight.toString())
            binding.etAge.setText(it.age.toString())
            binding.etBreed.setText(it.breed)
            binding.etGender.setText(it.gender)
            binding.etState.setText(it.state)
            binding.etMother.setText(it.motherMark)
            binding.etFather.setText(it.fatherMark)

        }
    }

    private fun deleteCow(user: String?, farmKey: String?, cowKey: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Vaca")
        builder.setMessage("¿Estás seguro de que quieres eliminar este animal?")

        builder.setPositiveButton("Sí") { _, _ ->
            firebaseInstance.deleteCow(user, farmKey, cowKey)
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()

            Toast.makeText(
                this@EditDeleteBreedingCowActivity,
                "Vaca eliminada exitosamente",
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