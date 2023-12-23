package com.jpdev.livestockproject.ui.Finance.sellAdapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.jpdev.livestockproject.databinding.ItemFarmBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Finance.RegisterSellCowActivity

class SellViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private val binding = ItemFarmBinding.bind(view)

    fun bind (cow: Cattle, cowKey: String, user: String, farmKey: String){
        val context = binding.cvFarm.context

        binding.tvTitle.text = cow.marking

        binding.cvFarm.setOnClickListener {
            val intent = Intent(context, RegisterSellCowActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("cowKey",cowKey)
            context.startActivity(intent)
        }
    }
}