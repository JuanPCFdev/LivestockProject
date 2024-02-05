package com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsBreeding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.BreedingPerformance

class BreedingAdapter(
    private val NewsList: List<BreedingPerformance>,
    val keys: List<String>,
    val userkey: String,
    val farmKey: String,
    val cowKey: String
) :
    RecyclerView.Adapter<BreedingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BreedingViewHolder(layoutInflater.inflate(R.layout.item_breeding, parent, false))
    }

    override fun getItemCount() = NewsList.size

    override fun onBindViewHolder(holder: BreedingViewHolder, position: Int) {
        val item = NewsList[position]
        val key = keys[position]
        holder.bind(item, key, userkey, farmKey, cowKey)
    }

}