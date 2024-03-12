package com.jpdev.livestockproject.ui.Cow.Consult.AdapterInsemination

import android.app.AlertDialog
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jpdev.livestockproject.data.network.FirebaseInstance
import com.jpdev.livestockproject.databinding.ItemInseminationBinding
import com.jpdev.livestockproject.domain.model.Insemination

class InseminationViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val binding = ItemInseminationBinding.bind(view)
    private lateinit var firebaseInstance: FirebaseInstance

    fun bind(
        insemination: Insemination,
        key: String,
        userKey: String,
        farmKey: String,
        cowKey: String
    ) {
        val context = binding.cvInsemination.context
        firebaseInstance = FirebaseInstance(context)
        binding.tvDate.text = insemination.inseminationDate

        binding.cvInsemination.setOnClickListener {

            firebaseInstance.getInseminationDetails(userKey, farmKey, cowKey, key) {

                val descripcion = "Fecha Inseminacion: ${it.inseminationDate}\n" +
                        "Decripción : ${it.descOfInsemination}\n" +
                        "Origen del esperma : ${it.spermOrigin}\n"

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Información sobre la monta")
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