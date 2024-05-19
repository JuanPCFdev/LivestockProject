package com.jpdev.livestockproject.ui.Cow.Monta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityConsultMontaBinding
import com.jpdev.livestockproject.databinding.ActivityMontaBinding
import com.jpdev.livestockproject.domain.model.Insemination
import com.jpdev.livestockproject.domain.model.LiftingPerformance
import com.jpdev.livestockproject.ui.Cow.Consult.AdapterInsemination.InseminationAdapter
import com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsLifting.LiftingAdapter
import com.jpdev.livestockproject.ui.Cow.Consult.CowResumeActivity

class ConsultMontaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultMontaBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var inseminationList = mutableListOf<Insemination>()
    private var Keys = mutableListOf<String>()
    private lateinit var adapter: InseminationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityConsultMontaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farm = intent.extras?.getString("farmKey")
        val cow = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)
        initListeners(user,farm,cow)
    }

    private fun initListeners(user: String?, farm: String?, cow: String?){
        getListCowInseminations(user.toString(), farm.toString(), cow.toString())
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
        binding.btnRegisterMonta.setOnClickListener {
            val intent = Intent(this, MontaActivity::class.java)
            intent.putExtra("userKey", user.toString())
            intent.putExtra("farmKey", farm.toString())
            intent.putExtra("cowKey", cow.toString())
            startActivity(intent)
            finish()
        }

    }

    private fun getListCowInseminations(user: String?, farm: String?, cowKey: String?) {
        firebaseInstance.getCowInseminationList(
            user.toString(),
            farm.toString(),
            cowKey.toString()
        ) { inseminations, keys ->
            inseminations?.let {
                inseminationList.clear()
                inseminationList.addAll(inseminations)
                keys?.let {
                    Keys.clear()
                    Keys.addAll(keys)
                    setUpRecyclerView(user.toString(), farm.toString(), cowKey.toString())
                }
            }
        }
    }

    private fun setUpRecyclerView(user: String?, farm: String?, cowKey: String?) {
        adapter =
            InseminationAdapter(inseminationList, Keys, user.toString(), farm.toString(), cowKey.toString())
        binding.rvMonta.adapter = adapter
        binding.rvMonta.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }
}