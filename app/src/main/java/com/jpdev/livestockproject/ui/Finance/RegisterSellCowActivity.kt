package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRegisterSellCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.Receipt
import java.text.DecimalFormat
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
        calculatePrice(user,farmKey, cowKey)
    }

    private fun printInfo(user: String?, farmKey: String?, cowKey: String?) {
        firebaseInstance.getCowDetails(user,farmKey,cowKey){
            binding.etMarcacion.setText(it.marking)
        }
    }

    private fun initListeners(user: String?, farmKey: String?, cowKey: String?) {
        binding.btnSaveSell.setOnClickListener {
            createReceipt(user, farmKey,cowKey)
        }
        binding.viewToolBar.back.setOnClickListener {
            back(user,farmKey)
        }
    }

    private fun createReceipt(key: String?, farmKey: String?, cowKey: String?){
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        if(binding.etPeso.text.toString().isNotBlank() &&
            binding.etPrecioVenta.text.toString().isNotBlank()&&
            binding.etNombreComprador.text.toString().isNotBlank()&&
            binding.etTelComprador.text.toString().isNotBlank()){
            val peso = binding.etPeso.text.toString().toIntOrNull()
            val precioVenta = binding.etPrecioVenta.text.toString().toDoubleOrNull()

            if (peso != null && precioVenta != null) {
                    val receipt = Receipt(
                        0,
                        binding.etMarcacion.text.toString(),
                        precioVenta,
                        formattedDate,
                        "Venta ganado",
                        binding.etNombreComprador.text.toString(),
                        binding.etTelComprador.text.toString()
                    )
                    firebaseInstance.registerReceiptBuy(receipt,key,farmKey)
                    updateCow(key, farmKey, cowKey)
                    Toast.makeText(this, "Se creo el recibo correctamente", Toast.LENGTH_SHORT).show()
                    back(key,farmKey)
                } else {
                    Toast.makeText(this, "Falta por llenar algun dato", Toast.LENGTH_SHORT).show()
            }
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


    private fun calculatePrice(user : String?, farmKey: String?, cowKey: String?){
        firebaseInstance.getCowDetails(user, farmKey, cowKey){
            firebaseInstance.getVaccineCostForSingleCow(user, farmKey, cowKey){totalVaccineCost ->
                firebaseInstance.getCountOfAliveAndUnsoldCows(user, farmKey){totalCowActive ->
                    firebaseInstance.getSumReceiptCowSingle(user, farmKey, cowKey){sumReceipt ->
                    val costCow: Double = it.cost
                    val prom = sumReceipt/totalCowActive
                    val sum = costCow+totalVaccineCost+prom
                        val formatter = DecimalFormat("#,###")
                        val formattedSum = formatter.format(sum)
                        binding.tvPriceEstimate.text = "Lo gastado aproximadamente en el animal fue: $formattedSum"

                    }
                }
            }
        }
    }

    private fun back(user : String?, farmKey: String?){
        val intent = Intent(this, SellCowActivity::class.java)
        intent.putExtra("userKey",user)
        intent.putExtra("farmKey",farmKey)
        startActivity(intent)
        finish()
    }
}