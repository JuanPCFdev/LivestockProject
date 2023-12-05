package com.jpdev.proyectoganadero.ui.User.Consult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jpdev.proyectoganadero.R
import com.jpdev.proyectoganadero.data.network.FirebaseInstance
import com.jpdev.proyectoganadero.databinding.ActivityUserBinding
import com.jpdev.proyectoganadero.domain.model.User
import com.jpdev.proyectoganadero.ui.Home.HomePageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val key = intent.extras?.getString("userKey")
        val keyFarm = intent.extras?.getString("farmKey")

        initListeners(key,keyFarm)
        getUserData(key)
    }

    private fun initListeners(key: String?,keyFarm:String?){
        binding.btnEditInfo.setOnClickListener {
            //Go to edit info
        }
        binding.btnChangePassword.setOnClickListener {
            //Go to change password
        }
        binding.btnGoToHome.setOnClickListener {
            val intent = Intent(this,HomePageActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",keyFarm)
            startActivity(intent)
            finish()
        }
    }

    private fun getUserData(key: String?) {
        if(!key.isNullOrEmpty()){//Si la llave no es nula
            GlobalScope.launch(Dispatchers.Main) {//Inicia una coroutine en hilo main
                try {
                    val user = firebaseInstance.getUser(key) //Obtengo el usuario con la key
                    if(user != null){//Si el usuario no es nulo
                        binding.tvUserName.text = user.name
                        binding.tvName.text = user.name
                        binding.tvPhone.text = user.phone
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