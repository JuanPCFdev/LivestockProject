package com.jpdev.livestockproject.ui.Farm.consult.adapterFarm

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.databinding.ItemFarmBinding
import com.jpdev.livestockproject.domain.model.Farm
import com.jpdev.livestockproject.ui.Home.HomePageActivity

class FarmViewHolder(view: View): RecyclerView.ViewHolder(view){

    private var binding = ItemFarmBinding.bind(view)
    fun bind(farm: Farm,farmKey:String,userKey:String){
        binding.tvFarm.text = farm.nameFarm

        val context = binding.cvFarm.context

        binding.cvFarm.setOnClickListener {
            val intent = Intent(context,HomePageActivity::class.java)
            intent.putExtra("userKey",userKey)
            intent.putExtra("farmKey",farmKey)
            context.startActivity(intent)
        }
    }
}