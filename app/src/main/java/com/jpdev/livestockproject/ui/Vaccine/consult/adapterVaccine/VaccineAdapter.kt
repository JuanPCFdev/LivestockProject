package com.jpdev.livestockproject.ui.Vaccine.consult.adapterVaccine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Cattle

class VaccineAdapter (
    private val cowList: List<Cattle>,
    val keys:List<String>,
    val userKey:String,
    val farmKey:String
    ) : RecyclerView.Adapter<VaccineViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return VaccineViewHolder(layoutInflater.inflate(R.layout.item_vaccinate, parent, false))
    }

    override fun getItemCount() = cowList.size

    override fun onBindViewHolder(holder: VaccineViewHolder, position: Int) {
        val item = cowList[position]
        val key = keys[position]
        holder.bind(item, key, userKey, farmKey)
    }

}