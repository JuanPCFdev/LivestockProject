package com.jpdev.livestockproject.ui.Cow.Consult.AdapterInsemination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Insemination

class InseminationAdapter (
    private val inseminationList: List<Insemination>,
    val keys: List<String>,
    val userkey: String,
    val farmKey: String,
    val cowKey: String
):RecyclerView.Adapter<InseminationViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InseminationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InseminationViewHolder(layoutInflater.inflate(R.layout.item_insemination, parent, false))
    }

    override fun getItemCount() = inseminationList.size
    override fun onBindViewHolder(holder: InseminationViewHolder, position: Int) {
        val item = inseminationList[position]
        val key = keys[position]
        holder.bind(item, key, userkey, farmKey, cowKey)
    }
}