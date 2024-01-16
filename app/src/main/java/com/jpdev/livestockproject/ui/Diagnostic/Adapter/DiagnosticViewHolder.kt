package com.jpdev.livestockproject.ui.Diagnostic.Adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jpdev.livestockproject.data.tips.Tips
import com.jpdev.livestockproject.databinding.ItemDiagnosticBinding
import com.jpdev.livestockproject.ui.Diagnostic.DiagnosticDetailsActivity

class DiagnosticViewHolder(view:View) :ViewHolder(view) {

    private val binding = ItemDiagnosticBinding.bind(view)

    fun bind(tips:Tips, user:String?, farmKey:String?){

        val context = binding.cvDiagnostic.context

        binding.tvTitle.text = tips.title

        binding.cvDiagnostic.setOnClickListener {
            val intent = Intent(context, DiagnosticDetailsActivity::class.java)
            intent.putExtra("userKey",user)
            intent.putExtra("farmKey",farmKey)
            intent.putExtra("title",tips.title)
            intent.putExtra("description",tips.Description)
            context.startActivity(intent)
        }
    }
}