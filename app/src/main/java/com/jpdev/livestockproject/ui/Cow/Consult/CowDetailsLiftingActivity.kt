package com.jpdev.livestockproject.ui.Cow.Consult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityCowDetailsBinding
import com.jpdev.livestockproject.domain.model.LiftingPerformance
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsLifting.LiftingAdapter
import com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsLifting.LiftingViewHolder
import com.jpdev.livestockproject.ui.Cow.HomeCow.HomeCowActivity
import com.jpdev.livestockproject.ui.Cow.Lifting.Register.RegisterNewsLiftingActivity
import com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.adapterRvVaccine.RvVaccineAdapter

class CowDetailsLiftingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCowDetailsBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var NewsList = mutableListOf<LiftingPerformance>()
    private var Keys = mutableListOf<String>()
    private lateinit var adapter: LiftingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCowDetailsBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")

        firebaseInstance = FirebaseInstance(this)

        printInfo(user, farmKey, cowKey)
        getListCowsNews(user, farmKey, cowKey)
        initListeners(user, farmKey, cowKey)
    }

    private fun printInfo(user: String?, farmKey: String?, cowKey: String?) {
        firebaseInstance.getCowDetails(user,farmKey,cowKey){
            val details = getString(R.string.register_news) + " ${it.marking}"
            binding.tvRegisteredCows.text = details
        }
    }

    private fun initListeners(user: String?, farmKey: String?, cowKey: String?) {
        binding.btnBack.setOnClickListener {
            val intent = Intent(this, HomeCowActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farmKey)
            startActivity(intent)
            finish()
        }
        binding.btnRegisterWeight.setOnClickListener {
            val intent = Intent(this, RegisterNewsLiftingActivity::class.java)
            intent.putExtra("userKey", user)
            intent.putExtra("farmKey", farmKey)
            intent.putExtra("cowKey", cowKey)
            startActivity(intent)
            finish()
        }
    }
    private fun getListCowsNews(user:String?,farm:String?,cowKey:String?){
        firebaseInstance.getCowNewsLifting(user.toString(),farm.toString(),cowKey.toString()){ news, keys ->
            if(news != null){
                news?.let {
                    NewsList.clear()
                    NewsList.addAll(news)
                    keys?.let{
                        Keys.clear()
                        Keys.addAll(keys)
                        setUpRecyclerView(user.toString(),farm.toString(),cowKey.toString())
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(user:String?,farm:String?,cowKey: String?){
        adapter = LiftingAdapter(NewsList, Keys, user.toString(), farm.toString(), cowKey.toString())
        binding.rvCows.adapter = adapter
        binding.rvCows.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }
}