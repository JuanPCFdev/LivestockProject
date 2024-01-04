package com.jpdev.livestockproject.ui.Finance.sellAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Cattle

class AdapterSell(
    private val cowsList: List<Cattle>,
    val Keys: List<String>,
    val userKey: String,
    val farmKey: String
) : RecyclerView.Adapter<SellViewHolder>() {

    // Filtrar las vacas vendidas
    private val filteredCowsList: List<Cattle> = cowsList.filter { it.state != "vendido" }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SellViewHolder(layoutInflater.inflate(R.layout.item_farm, parent, false))
    }

    override fun getItemCount() = filteredCowsList.size

    override fun onBindViewHolder(holder: SellViewHolder, position: Int) {
        val item = filteredCowsList[position]
        val key = Keys[cowsList.indexOf(item)] // Obtener la clave original de la lista completa
        holder.bind(item, key, userKey, farmKey)
    }
}