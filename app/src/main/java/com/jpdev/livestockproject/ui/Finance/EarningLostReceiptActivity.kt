package com.jpdev.livestockproject.ui.Finance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityBuyBinding
import com.jpdev.livestockproject.databinding.ActivityEarningLostReceiptBinding
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class EarningLostReceiptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEarningLostReceiptBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var cost: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarningLostReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)
        val key = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        firebaseInstance.getFarmDetails(key.toString(),farmKey.toString()){
            binding.tvTitle.text = it?.nameFarm
        }
        initComponents(key, farmKey)
    }

    private fun initComponents(key: String?, farmKey: String?) {
        binding.btnHomePage.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            intent.putExtra("userKey",key)
            intent.putExtra("farmKey",farmKey)
            startActivity(intent)

            finish()
        }

        calculateFinances(key, farmKey)

    }
    private fun calculateFinances(key: String?, farmKey: String?){
        firebaseInstance.getTotalVaccineCostForAllCows(key,farmKey){totalVaccineCost ->
            binding.etVaccine.text = totalVaccineCost.toString()
            firebaseInstance.getSumOfAmountPaidByReceiptType(key,farmKey){ totalAmountPaidCompra, totalAmountPaidVenta ->
                Log.d("TotalAmountPaidCompra", "Total amount paid for compra: $totalAmountPaidCompra")
                Log.d("TotalAmountPaidVenta", "Total amount paid for venta: $totalAmountPaidVenta")
                binding.etWon.text = totalAmountPaidVenta.toString()
                binding.etinvestment.text = totalAmountPaidCompra.toString()

                cost = totalAmountPaidVenta - (totalAmountPaidCompra + totalVaccineCost)

                if (cost == 0.0 ){
                    binding.etAbstract.text = getString(R.string.AbstractNothing)
                }else if (cost > 0.0){
                    binding.etAbstract.text = getString(R.string.AbstractWon)+" $cost"
                }else if (cost< 0.0){
                    binding.etAbstract.text = getString(R.string.AbstractLost)+" $cost"
                }

            }
        }

    }
}


