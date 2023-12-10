package com.jpdev.livestockproject.ui.Finance.sellAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Cattle

class AdapterSell (private val cowsList: List<Cattle>, val Keys: List<String>, val userKey: String, val farmKey: String): RecyclerView.Adapter<SellViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SellViewHolder(layoutInflater.inflate(R.layout.item_farm, parent, false))
    }

    override fun getItemCount() = cowsList.size

    override fun onBindViewHolder(holder: SellViewHolder, position: Int) {
        val item = cowsList[position]
        val key = Keys[position]
        holder.bind(item, key)
    }

}