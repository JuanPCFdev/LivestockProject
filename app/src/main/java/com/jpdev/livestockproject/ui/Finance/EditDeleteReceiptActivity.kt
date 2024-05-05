package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityEditDeleteReceiptBinding
import com.jpdev.livestockproject.domain.model.DatePickerFragment
import com.jpdev.livestockproject.domain.model.Receipt


class EditDeleteReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditDeleteReceiptBinding
    private lateinit var firebaseInstance: FirebaseInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDeleteReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val receiptKey = intent.extras?.getString("ReceiptKey")
        printInfo(key,farmKey,receiptKey)
        initlistenener(key,farmKey,receiptKey)
    }

    private fun initlistenener(key: String?, farmKey: String?, receiptKey: String?) {
        binding.viewToolBar.back.setOnClickListener {
            back(key, farmKey, receiptKey)
        }
        binding.btnEdit.setOnClickListener {
            editReceipt(key, farmKey, receiptKey)
        }
        binding.etReceiptData.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "Fecha de Nacimiento")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val mes = month + 1
        binding.etReceiptData.setText("$day/$mes/$year")

    }

    private fun printInfo(user: String?, farmKey: String?, receiptKey: String?) {
        firebaseInstance.getReceiptDetails(user, farmKey, receiptKey) {
            binding.tvTitle.text = it.receiptType
            binding.etReceiptName.setText(it.nameReceipt)
            binding.etReceiptPrice.setText(it.amountPaid.toString())
            binding.etReceiptData.setText(it.date)
        }
    }

    private fun editReceipt(key: String?, farmKey: String?, receiptKey: String?) {
        firebaseInstance.getReceiptDetails(key, farmKey, receiptKey) { originalReceipt ->
            Log.d("OriginalReceiptType", originalReceipt?.javaClass?.simpleName ?: "null")
            val updatedName = binding.etReceiptName.text.toString()
            val updatedPrice = binding.etReceiptPrice.text.toString().toDouble()
            val updatedData = binding.etReceiptData.text.toString()

            if (originalReceipt.nameReceipt != updatedName ||
                originalReceipt.amountPaid != updatedPrice ||
                originalReceipt.date != updatedData
            ) {

                val updatedReceipt = Receipt(
                    0,
                    updatedName,
                    updatedPrice,
                    updatedData,
                    originalReceipt.receiptType,
                    originalReceipt.name,
                    originalReceipt.tel
                )
                firebaseInstance.editReceipt(updatedReceipt, key, farmKey, receiptKey)
                Toast.makeText(
                    this@EditDeleteReceiptActivity,
                    "Información del recibo actualizada exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
                back(key, farmKey, receiptKey)

            } else {
                Toast.makeText(this, "No se realizaron cambios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun back(key: String?, farmKey: String?, receiptKey: String?) {
        val intent = Intent(this, ConsultReceiptActivity::class.java)
        intent.putExtra("userKey", key)
        intent.putExtra("farmKey", farmKey)
        intent.putExtra("ReceiptKey", receiptKey)
        startActivity(intent)
        finish()
    }
}