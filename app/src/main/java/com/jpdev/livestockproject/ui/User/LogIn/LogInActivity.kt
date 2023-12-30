package com.jpdev.livestockproject.ui.User.LogIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jpdev.livestockproject.databinding.ActivityLogInBinding
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.domain.model.User
import com.jpdev.livestockproject.ui.Farm.consult.FarmActivity
import com.jpdev.livestockproject.ui.User.Register.RegisterUserActivity

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private lateinit var userList : List<Pair<String, User>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        firebaseInstance = FirebaseInstance(this)
        getUsers()
        initListeners()
    }

    private fun initListeners(){
        binding.btnLogin.setOnClickListener { goToHomePage() }
        binding.btnPassRecord.setOnClickListener {  }
        binding.btnRegister.setOnClickListener { Register() }
    }

    private fun Register(){
        val intent = Intent(this,RegisterUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToHomePage(){
        var key:String = ""
        if(validateCredentials()){
            userList.forEach{ user->
                if(binding.etUser.text.toString() == user.second.name && binding.etPassword.text.toString() == user.second.password){
                    key = user.first
                }
            }
            if(key!=""){
                HomePage(key)
            }else{
                Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getUsers(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = firebaseInstance.getCleanSnapshot(snapshot)
                userList = list

                if (userList == null) {
                    userList = emptyList()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.i("Algo fallo :p", error.details)
                userList = emptyList()
            }
        }
        firebaseInstance.setupDatabaseListener(postListener)
    }

    private fun validateCredentials():Boolean{
        var success = true

        if(binding.etUser.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Nombre invalido", Toast.LENGTH_SHORT).show()
            success = false
        }else if(binding.etPassword.text.toString().isNullOrEmpty()){
            Toast.makeText(this, "Contraseña invalida", Toast.LENGTH_SHORT).show()
            success = false
        }

        return success
    }

    private fun HomePage(key:String){
        val intent = Intent(this,FarmActivity::class.java)
        intent.putExtra("userKey",key)
        startActivity(intent)
        finish()
    }

}