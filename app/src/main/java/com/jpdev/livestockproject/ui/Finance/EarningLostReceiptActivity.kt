package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityEarningLostReceiptBinding
import java.text.DecimalFormat

class EarningLostReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEarningLostReceiptBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var cost: Double = 0.0
    val formatter = DecimalFormat("#,###")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarningLostReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        firebaseInstance.getFarmDetails(key.toString(), farmKey.toString()) {
            binding.tvTitle.text = it?.nameFarm
        }
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        binding.viewToolBar.back.setOnClickListener {
            val intent = Intent(this, FinanceActivity::class.java)
            intent.putExtra("userKey", key)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
            finish()
        }
        calculateFinances(key, farmKey)

    }

    private fun calculateFinances(key: String?, farmKey: String?) {
        firebaseInstance.getTotalVaccineCostForAllCows(key, farmKey) { totalVaccineCost ->
            val formattedvaccine = formatter.format(totalVaccineCost)
            binding.etVaccine.text = formattedvaccine
            firebaseInstance.getSumOfAmountPaidByReceiptType(
                key,
                farmKey
            ) { totalAmountPaidCompra, totalAmountPaidVenta ->

                Log.d(
                    "TotalAmountPaidCompra",
                    "Total amount paid for compra: $totalAmountPaidCompra"
                )
                Log.d("TotalAmountPaidVenta", "Total amount paid for venta: $totalAmountPaidVenta")
                val formattedVenta = formatter.format(totalAmountPaidVenta)
                binding.etWon.text = formattedVenta.toString()
                val formattedCompra = formatter.format(totalAmountPaidCompra)
                binding.etinvestment.text = formattedCompra.toString()

                cost = totalAmountPaidVenta - (totalAmountPaidCompra + totalVaccineCost)

                val formattedCost = formatter.format(cost)

                if (cost == 0.0) {
                    binding.etAbstract.text = getString(R.string.AbstractNothing)
                } else if (cost > 0.0) {
                    binding.etAbstract.text = getString(R.string.AbstractWon) + " $formattedCost"
                } else if (cost < 0.0) {
                    binding.etAbstract.text = getString(R.string.AbstractLost) + " $formattedCost"
                }

            }
        }

    }
}


