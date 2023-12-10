package com.jpdev.livestockproject.ui.Finance.sellAdapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.jpdev.livestockproject.databinding.ItemFarmBinding
import com.jpdev.livestockproject.domain.model.Cattle

class SellViewHolder(view: View) : RecyclerView.ViewHolder(view){

    private val binding = ItemFarmBinding.bind(view)

    fun bind (cow: Cattle, key: String){
        binding.tvTitle.text = cow.marking
    }
}