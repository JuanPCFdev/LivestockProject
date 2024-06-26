package com.jpdev.livestockproject.ui.User.Register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterUserBinding
import com.jpdev.livestockproject.domain.model.Farm
import com.jpdev.livestockproject.domain.model.User
import com.jpdev.livestockproject.ui.User.LogIn.LogInActivity

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private lateinit var idList : List<Pair<String,User>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        getId()
        initListeners()
    }

    private fun initListeners(){
        binding.btnRegisterUser.setOnClickListener {
            Register()
        }
        binding.viewToolBar.back.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin(){
        val intent = Intent(this,LogInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun Register(){
        if(validateCredentials() && validateName()){
            val newUser = User(
                idList.size,
                binding.etName.text.toString(),
                binding.etPassword.text.toString(),
                binding.etPhone.text.toString(),
                emptyList<Farm>().toMutableList()
                )
            firebaseInstance.writeOnFirebase(newUser)
        }
        if(validateCredentials() && validateName()){
            goToLogin()
        }

    }

    private fun getId(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = firebaseInstance.getCleanSnapshot(snapshot)
                idList = list
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo :p", error.details)
            }
        }
        firebaseInstance.setupDatabaseListener(postListener)
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
    }

    private fun validateName():Boolean{
        var success = true

        idList.forEach{ user ->
            if(binding.etName.text.toString() == user.second.name ||
                binding.etPhone.text.toString() == user.second.phone){
                Toast.makeText(this, "El nombre de usuario o telefono estan en uso", Toast.LENGTH_SHORT).show()
                success = false
            }
        }

        return success
    }

    private fun validateCredentials():Boolean{
        var success = true

        if(binding.etName.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Nombre invalido", Toast.LENGTH_SHORT).show()
            success = false
        }
        if(binding.etPhone.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Telefono invalido", Toast.LENGTH_SHORT).show()
            success = false
        }
        if(binding.etPassword.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Contraseña invalida", Toast.LENGTH_SHORT).show()
            success = false
        }
        if(binding.etConfirmPassword.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Confirmacion invalida", Toast.LENGTH_SHORT).show()
            success = false
        }
        if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            success = false
        }

        return success
    }

}