package com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.adapterRvVaccine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.R
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Cow.Consult.Adapter.CowViewHolder

class RvVaccineAdapter
    (
    private val vaccineList: List<Vaccine>,
    val vaccineKeys: List<String>,
    val userKey: String,
    val farmKey: String,
    val cowKey: String
) : RecyclerView.Adapter<RvVaccineViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvVaccineViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RvVaccineViewHolder(layoutInflater.inflate(R.layout.item_vaccine, parent, false))
    }

    override fun getItemCount() = vaccineList.size

    override fun onBindViewHolder(holder: RvVaccineViewHolder, position: Int) {
        val item = vaccineList[position]
        val key = vaccineKeys[position]
        holder.bind(item, key, userKey, farmKey, cowKey)
    }

}