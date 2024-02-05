package com.jpdev.livestockproject.ui.Cow.Consult.AdapterNewsLifting

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ItemLiftingBinding
import com.jpdev.livestockproject.domain.model.LiftingPerformance

class LiftingViewHolder(view: View) : ViewHolder(view) {
    private val binding = ItemLiftingBinding.bind(view)
    private lateinit var firebaseInstance: FirebaseInstance

    fun bind(
        News: LiftingPerformance,
        key: String,
        userKey: String,
        farmKey: String,
        cowKey: String
    ) {
        val context = binding.cvLifting.context
        firebaseInstance = FirebaseInstance(context)
        binding.tvdate.text = News.PLDate
        binding.tvWeight.text = News.PLWeight.toString()

        binding.cvLifting.setOnClickListener {
            firebaseInstance.getNewsLiftingDetails(userKey, farmKey, cowKey, key) {
                val descripcion = "Fecha registro peso: ${it.PLDate}\n" +
                        "Peso: ${it.PLWeight}\n" +
                        "Dieta: ${it.PLDiet}\n"

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