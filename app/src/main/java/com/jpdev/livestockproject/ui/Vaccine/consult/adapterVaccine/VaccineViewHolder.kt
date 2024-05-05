package com.jpdev.livestockproject.ui.Vaccine.consult.adapterVaccine

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jpdev.livestockproject.databinding.ItemVaccinateBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Vaccine.consult.rvVaccine.RvVaccineActivity

class VaccineViewHolder(view:View) : ViewHolder(view) {

    private val binding = ItemVaccinateBinding.bind(view)

    fun bind(cow: Cattle, cowKey: String, user: String, farmKey: String){

        val context = binding.cvCow.context

        binding.tvWeight.text = cow.weight.toString()
        binding.tvMarking.text = cow.marking
        binding.tvGender.text = cow.gender

        binding.cvCow.setOnClickListener {
            val intent = Intent(context, RvVaccineActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("cowKey",cowKey)
            context.startActivity(intent)
        }
    }
}