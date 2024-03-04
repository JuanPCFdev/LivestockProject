package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterSellCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.Receipt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterSellCowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterSellCowBinding
    private lateinit var firebaseInstance: FirebaseInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSellCowBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")
        firebaseInstance = FirebaseInstance(this)
        printInfo(user, farmKey, cowKey)
        initListeners(user,farmKey, cowKey)
    }

    private fun printInfo(user: String?, farmKey: String?, cowKey: String?) {
        firebaseInstance.getCowDetails(user,farmKey,cowKey){
            binding.etMarcacion.setText(it.marking)
        }
    }

    private fun initListeners(user: String?, farmKey: String?, cowKey: String?) {
        binding.btnSaveSell.setOnClickListener {
            createReceipt(user, farmKey)
            updateCow(user, farmKey, cowKey)
        }
        binding.btnHomePage.setOnClickListener {
            val intent = Intent(this, SellCowActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
            finish()
        }
    }

    private fun createReceipt(key: String?, farmKey: String?){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        if(validateCredentials()){
            val receipt = Receipt(
                0,
                binding.etMarcacion.text.toString(),
                binding.etPrecioVenta.text.toString().toDouble(),
                formattedDate,
                "Venta ganado",
                binding.etNombreComprador.text.toString(),
                binding.etTelComprador.text.toString()
            )

            firebaseInstance.registerReceiptBuy(receipt,key,farmKey)
            Toast.makeText(this, "Se creo el recibo correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SellCowActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Falta por llenar algun dato", Toast.LENGTH_SHORT).show()
        }
    }
  private fun updateCow(user : String?, farmKey: String?, cowKey: String?){
        firebaseInstance.getCowDetails(user, farmKey, cowKey) {
            val updatedCow = Cattle(
                0,
                it.marking,
                it.birthdate,
                binding.etPeso.text.toString().toInt(),
                it.age,
                it.breed,
                "vendido",
                it.gender,
                it.type,
                it.motherMark,
                it.fatherMark,
                it.cost,
                it.castrated
            )

            firebaseInstance.editCow(updatedCow, user, farmKey, cowKey)
        }

    }
    private fun validateCredentials():Boolean {
        var success = false

        if(binding.etPeso.text.toString().isNotEmpty()
            && binding.etPrecioVenta.text.toString().isNotEmpty()
            && binding.etNombreComprador.text.toString().isNotEmpty()
            && binding.etTelComprador.text.toString().isNotEmpty()
        ){
            success = true
        }

        return success
    }

    private fun calculatePrice(user : String?, farmKey: String?, cowKey: String?){
        var cost : Double
        
    }
}