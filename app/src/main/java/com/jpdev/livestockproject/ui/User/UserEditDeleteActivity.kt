package com.jpdev.livestockproject.ui.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityUserEditDeleteBinding
import com.jpdev.livestockproject.domain.model.Farm
import com.jpdev.livestockproject.domain.model.User
import com.jpdev.livestockproject.ui.Farm.consult.FarmActivity
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import com.jpdev.livestockproject.ui.User.Consult.UserActivity
import com.jpdev.livestockproject.ui.User.LogIn.LogInActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserEditDeleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserEditDeleteBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserEditDeleteBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        getUserData(key)
        initListeners(key, farmKey)
    }

    private fun initListeners(key: String?, farmKey: String?) {
        binding.viewToolBar.back.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnSaveChanges.setOnClickListener {
            saveChanges(key, farmKey)
        }
        binding.btnDeleteUser.setOnClickListener {
            deleteUser(key)
        }
    }

    private fun saveChanges(key:String?,farmKey:String?){

        val updatedUser = User(id,
            binding.etUsername.text.toString(),
            binding.etPassword.text.toString(),
            binding.etPhone.text.toString())

        firebaseInstance.editUser(updatedUser, key)

        Toast.makeText(this,"Se han actualizado los datos",Toast.LENGTH_SHORT).show()

        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra("userKey",key)
        intent.putExtra("farmKey",farmKey)
        startActivity(intent)
        finish()
    }

    private fun deleteUser(key:String?){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Usuario")
        builder.setMessage("¿Estás seguro de que quieres eliminar este usuario?")

        builder.setPositiveButton("Sí") { _, _ ->
            firebaseInstance.deleteUser(key)
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
            Toast.makeText(
                this@UserEditDeleteActivity,
                "Usuario eliminado exitosamente",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ ->
            // No hace nada
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun getUserData(key: String?) {
        if(!key.isNullOrEmpty()){//Si la llave no es nula
            GlobalScope.launch(Dispatchers.Main) {//Inicia una coroutine en hilo main
                try {
                    val user = firebaseInstance.getUser(key) //Obtengo el usuario con la key
                    if(user != null){//Si el usuario no es nulo
                        binding.etUsername.setText(user.name)
                        binding.etPassword.setText(user.password)
                        binding.etPhone.setText(user.phone)
                        id = user.userId
                    }else{//El usuario es nulo
                        Log.i("Algo fallo", "El usuario es nulo")
                    }
                }catch (e:Exception){//Mensaje de error en try
                    Log.i("Algo fallo", e.message.toString())
                }
            }
        }else{
            Log.i("Algo fallo","El usuario no existe")
        }
    }
}