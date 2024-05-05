package com.jpdev.livestockproject.ui.Cow.Consult.Adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jpdev.livestockproject.databinding.ItemCowBinding
import com.jpdev.livestockproject.domain.model.Cattle
import com.jpdev.livestockproject.ui.Cow.Consult.CowResumeActivity

class CowViewHolder(view: View) : ViewHolder(view) {

    private val binding = ItemCowBinding.bind(view)
    fun bind(cow: Cattle, cowKey: String, user: String, farmKey: String, type: String) {

        val context = binding.cvCow.context

        binding.tvWeight.text = cow.weight.toString()
        binding.tvMarking.text = cow.marking
        binding.tvGender.text = cow.gender

        binding.cvCow.setOnClickListener {
            val intent = Intent(context, CowResumeActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("cowKey",cowKey)
            context.startActivity(intent)
        }

    }

}