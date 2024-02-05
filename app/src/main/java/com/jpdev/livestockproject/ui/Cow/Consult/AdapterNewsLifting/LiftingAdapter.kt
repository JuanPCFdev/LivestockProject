package com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsLifting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.LiftingPerformance

class LiftingAdapter(
    private val NewsList: List<LiftingPerformance>,
    val keys: List<String>,
    val userkey: String,
    val farmKey: String,
    val cowKey: String
) :
    RecyclerView.Adapter<LiftingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiftingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return LiftingViewHolder(layoutInflater.inflate(R.layout.item_lifting, parent, false))
    }

    override fun getItemCount() = NewsList.size

    override fun onBindViewHolder(holder: LiftingViewHolder, position: Int) {
        val item = NewsList[position]
        val key = keys[position]
        holder.bind(item, key, userkey, farmKey, cowKey)
    }

}