package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityConsultReceiptBinding
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class ConsultReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultReceiptBinding
    private lateinit var firebaseInstance: FirebaseInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val receiptKey = intent.extras?.getString("ReceiptKey")
        printInfo(key,farmKey,receiptKey)
        initComponents(key,farmKey,receiptKey)
    }

    private fun initComponents(key: String?,farmKey: String?,receiptKey: String?){
        binding.btnDelete.setOnClickListener {
            deleteReceipt(key,farmKey,receiptKey)
        }
        binding.btnHomePage.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, EditDeleteReceiptActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("ReceiptKey", receiptKey)
            startActivity(intent)
            finish()
        }
        binding.cardDetailsReceipt
    }

    private fun printInfo(key: String?,farmKey: String?,receiptKey: String?){
        firebaseInstance.getReceiptDetails(key,farmKey,receiptKey){
            val receipt = "Recibo de : ${it.nameReceipt}\n" +
                    "Valor del recibo : ${it.amountPaid}\n" +
                    "Fecha del recibo : ${it.date}\n" +
                    "Tipo de recibo : ${it.receiptType}\n" +
                    "Nombre : ${it.name}\n" +
                    "Telefono : ${it.tel}\n"

            binding.tvReceiptDetails.text = receipt
            binding.tvTitle.text = it.receiptType
        }
    }


   private fun deleteReceipt(user: String?, farmKey: String?, receiptKey: String?) {
       // Obtener detalles del recibo, incluido el tipo
       firebaseInstance.getReceiptDetails(user, farmKey, receiptKey) { receipt ->
           val receiptType = receipt.receiptType
           val receiptName = receipt.nameReceipt

           val builder = AlertDialog.Builder(this)
           builder.setTitle("Eliminar Recibo")
           builder.setMessage("¿Estás seguro de que quieres eliminar este recibo?, si se elimina ya no se tomará en cuenta para hacer las cuentas de la finca, en caso de ser un recibo de compra de ganado se eliminara la vaca")

           builder.setPositiveButton("Sí") { _, _ ->
               when (receiptType) {
                   "Compra ganado" -> {
                       // Eliminar la vaca y el recibo
                       firebaseInstance.deleteReceiptAndCowByCommonName(user, farmKey, receiptName)
                   }
                   else -> {
                       // Acción por defecto (eliminar solo el recibo)
                       firebaseInstance.deleteReceipt(user, farmKey, receiptKey)
                   }
               }

               val intent = Intent(this, HomePageActivity::class.java)
               intent.putExtra("userKey", user)
               intent.putExtra("farmKey", farmKey)
               startActivity(intent)
               finish()

               Toast.makeText(
                   this@ConsultReceiptActivity,
                   "Recibo eliminado exitosamente",
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