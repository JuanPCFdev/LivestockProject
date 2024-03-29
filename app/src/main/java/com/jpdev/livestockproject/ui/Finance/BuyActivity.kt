package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityBuyBinding
import com.jpdev.livestockproject.domain.model.Receipt
import com.jpdev.livestockproject.ui.Cow.Lifting.Register.RegisterCowActivity

class BuyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        binding.btnReturnHome.setOnClickListener {
            val intent = Intent(this, FinanceActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnBuyCow.setOnClickListener {
            val intent = Intent(this, RegisterCowActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnSaveBuy.setOnClickListener {
            createReceipt(key,farmKey)
        }
    }

    private fun createReceipt(key: String?, farmKey: String?){
        if(validateCredentials()){
            val receipt = Receipt(
                0,
                binding.etNameProducto.text.toString(),
                binding.etPrecioProducto.text.toString().toDouble(),
                binding.etFechaCompra.text.toString(),
                receiptType = "Compra producto",
                binding.etNombreProveedor.text.toString(),
                binding.etTelProveedor.text.toString()
            )

            firebaseInstance.registerReceiptBuy(receipt,key,farmKey)
            Toast.makeText(this, "Se creo el recibo correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FinanceActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Falta por llenar algun dato", Toast.LENGTH_SHORT).show()
        }
    }
    private fun validateCredentials():Boolean {
        var success = false

        if(binding.etDesProducto.text.toString().isNotEmpty()
            && binding.etFechaCompra.text.toString().isNotEmpty()
            && binding.etNameProducto.text.toString().isNotEmpty()
            && binding.etNombreProveedor.text.toString().isNotEmpty()
            && binding.etPrecioProducto.text.toString().isNotEmpty()
            && binding.etTelProveedor.text.toString().isNotEmpty()
        ){
            success = true
        }

        return success
    }

}