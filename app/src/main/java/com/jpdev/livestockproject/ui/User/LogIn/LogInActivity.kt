package com.jpdev.livestockproject.ui.User.LogIn

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityLogInBinding
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
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        if(isNetworkAvailable()){
            getUsers()
            initListeners()
        }else{
            showNoInternetToast()
        }
    }

    private fun isNetworkAvailable():Boolean{
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showNoInternetToast() {
        Toast.makeText(
            this,
            "No hay conexión a Internet. Por favor, conéctese a una red.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun initListeners(){
        binding.btnLogin.setOnClickListener { goToHomePage() }
        binding.btnRegister.setOnClickListener { Register() }
        binding.btnHelp.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=zFpK6Q6Fk44&t=3s&ab_channel=mr.J"
            openVideo(url)
        }
    }

    private fun Register(){
        val intent = Intent(this,RegisterUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToHomePage(){
        var key = ""
        if(validateCredentials()){
            userList.forEach{ user->
                if(binding.etUser.text.toString() == user.second.name && binding.etPassword.text.toString() == user.second.password){
                    key = user.first
                }
            }
            if(key!=""){
                homePage(key)
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

    private fun homePage(key:String){
        val intent = Intent(this,FarmActivity::class.java)
        intent.putExtra("userKey",key)
        startActivity(intent)
    }
    private fun openVideo(url: String) {
        try {
            val videoUri = url
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUri))
            if (videoUri!= null) {
                startActivity(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error, asegurese de tener youtube", Toast.LENGTH_SHORT).show()
        }
    }



}