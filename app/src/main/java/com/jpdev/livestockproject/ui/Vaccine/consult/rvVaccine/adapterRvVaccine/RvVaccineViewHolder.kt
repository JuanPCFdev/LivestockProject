package com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.adapterRvVaccine

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jpdev.livestockproject.databinding.ItemVaccineBinding
import com.jpdev.livestockproject.domain.model.Vaccine
import com.jpdev.livestockproject.ui.Vaccine.consult.VaccineDetailsActivity

class RvVaccineViewHolder(view:View):ViewHolder(view) {
    private val binding = ItemVaccineBinding.bind(view)
    fun bind(vaccine: Vaccine, key: String, userKey: String, farmKey: String, cowKey: String) {
        val context = binding.cvVaccine.context

        binding.tvName.text = vaccine.vacineName
        binding.tvCost.text = vaccine.vaccineCost.toString()

        binding.consult.setOnClickListener {
            val intent = Intent(context,VaccineDetailsActivity::class.java)
            intent.putExtra("userKey",userKey)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("cowKey",cowKey)
            intent.putExtra("vaccineKey",key)
            context.startActivity(intent)
        }

        binding.edit.setOnClickListener {

        }

    }
}