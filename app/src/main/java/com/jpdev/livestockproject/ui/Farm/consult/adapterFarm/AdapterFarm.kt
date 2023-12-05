package com.jpdev.livestockproject.ui.Farm.consult.adapterFarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Farm

class adapterFarm (val Farm : List<Farm>,val Keys: List<String>,val userKey:String):
    RecyclerView.Adapter<FarmViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FarmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FarmViewHolder(layoutInflater.inflate(R.layout.item_farm,parent,false))
    }

    override fun getItemCount() = Farm.size

    override fun onBindViewHolder(holder: FarmViewHolder, position: Int) {
        val item = Farm[position]
        val key = Keys[position]
        val user = userKey
        holder.bind(item,key,user)
    }

}