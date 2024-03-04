package com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsBreeding

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ItemBreedingBinding
import com.jpdev.livestockproject.domain.model.BreedingPerformance

class BreedingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemBreedingBinding.bind(view)
    private lateinit var firebaseInstance: FirebaseInstance


    fun bind(
        News: BreedingPerformance,
        key: String,
        userKey: String,
        farmKey: String,
        cowKey: String
    ) {
        val context = binding.cvBreeding.context
        firebaseInstance = FirebaseInstance(context)
        binding.tvdate.text = News.PBDate
        binding.tvdateInse.text = News.PBDateInsemination
        binding.tvWeight.text = News.PBInitialWeight.toString()

        binding.cvBreeding.setOnClickListener {
            firebaseInstance.getNewsBreedingDetails(userKey, farmKey, cowKey, key) {
                var txtConfirm = "NO"
                if (it.PBDeath){
                    txtConfirm = "SI"
                }
                val descripcion = "Fecha Nacimiento cria: ${it.PBDate}\n" +
                        "Fecha inseminación: ${it.PBDateInsemination}\n" +
                        "peso inicial cria: ${it.PBInitialWeight}\n" +
                        "Dieta: ${it.PBDiet}\n" +
                        "Muerta $txtConfirm\n"

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Información sobre la nota")
                builder.setMessage(
                    descripcion
                )

                builder.setPositiveButton("Volver") { _, _ ->

                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }

        }
    }
}