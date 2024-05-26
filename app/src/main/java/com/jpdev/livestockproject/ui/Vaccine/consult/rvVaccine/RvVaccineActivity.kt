package com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ActivityRvVaccineBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Cow.Consult.Adapter.CowAdapter
import com.jpdev.livestockproject.ui.Home.HomePageActivity
import com.jpdev.livestockproject.ui.Vaccine.consult.ConsultVaccineActivity
import com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.adapterRvVaccine.RvVaccineAdapter
import com.jpdev.livestockproject.ui.Vaccine.register.RegisterVaccineActivity

class RvVaccineActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRvVaccineBinding
    private lateinit var firebaseInstance: FirebaseInstance
    private var vaccineList = mutableListOf<Vaccine>()
    private var vaccineKeys = mutableListOf<String>()
    private lateinit var adapter: RvVaccineAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRvVaccineBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)
        firebaseInstance = FirebaseInstance(this)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")
        getListCowsVaccines(user,farmKey,cowKey)
        initListeners(user,farmKey,cowKey)
    }

    override fun onResume() {
        super.onResume()
        binding = ActivityRvVaccineBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        val user = intent.extras?.getString("userKey")
        val farmKey = intent.extras?.getString("farmKey")
        val cowKey = intent.extras?.getString("cowKey")
        getListCowsVaccines(user,farmKey,cowKey)
        initListeners(user,farmKey,cowKey)
    }

    private fun initListeners(user: String?,farm: String?,cowKey: String?){
        binding.btnRegisterVaccine.setOnClickListener {
            val intent = Intent(this,RegisterVaccineActivity::class.java)
            intent.putExtra("userKey",user.toString())
            intent.putExtra("farmKey",farm.toString())
            intent.putExtra("cowKey",cowKey.toString())
            startActivity(intent)
        }
        binding.viewToolBar.back.setOnClickListener {
            finish()
        }
    }

    private fun getListCowsVaccines(user:String?,farm:String?,cowKey:String?){
        firebaseInstance.getCowVaccines(user.toString(),farm.toString(),cowKey.toString()){ vaccines, keys ->
            if(vaccines != null){
                vaccines?.let {
                    vaccineList.clear()
                    vaccineList.addAll(vaccines)
                    keys?.let{
                        vaccineKeys.clear()
                        vaccineKeys.addAll(keys)
                        setUpRecyclerView(user.toString(),farm.toString(),cowKey.toString())
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView(user:String?,farm:String?,cowKey: String?){
        adapter = RvVaccineAdapter(vaccineList,vaccineKeys,user.toString(),farm.toString(),cowKey.toString())
        binding.rvVaccines.adapter = adapter
        binding.rvVaccines.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()
    }

}

