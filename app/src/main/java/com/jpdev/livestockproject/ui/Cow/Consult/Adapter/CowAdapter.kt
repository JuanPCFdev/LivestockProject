package com.jpdev.livestockproject.ui.Cow.Consult.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Cattle

class CowAdapter(
    private val cowsList: List<Cattle>,
    val Keys: List<String>,
    val userKey: String,
    val farmKey: String
) :
    RecyclerView.Adapter<CowViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CowViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CowViewHolder(layoutInflater.inflate(R.layout.item_cow, parent, false))
    }

    override fun getItemCount() = cowsList.size

    override fun onBindViewHolder(holder: CowViewHolder, position: Int) {
        val item = cowsList[position]
        val key = Keys[position]
        val type = cowsList[position].type
        holder.bind(item, key, userKey, farmKey, type)
    }
}