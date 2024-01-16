package com.jpdev.livestockproject.ui.Diagnostic.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.data.tips.Tips

class DiagnosticAdapter(private val tipsList: List<Tips>, val user: String, val farm: String) :
    RecyclerView.Adapter<DiagnosticViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosticViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DiagnosticViewHolder(layoutInflater.inflate(R.layout.item_diagnostic, parent, false))
    }

    override fun getItemCount() = tipsList.size
    override fun onBindViewHolder(holder: DiagnosticViewHolder, position: Int) {
        val item = tipsList[position]
        holder.bind(item, user, farm)
    }
}