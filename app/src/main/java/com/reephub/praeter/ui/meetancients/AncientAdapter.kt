package com.reephub.praeter.ui.meetancients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reephub.praeter.data.remote.dto.AncientDto
import com.reephub.praeter.databinding.RowAncientBinding

class AncientAdapter(val items: List<AncientDto>) : RecyclerView.Adapter<RowAncientViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowAncientViewHolder {
        val rowBinding =
            RowAncientBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RowAncientViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: RowAncientViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size ?: 0
    }

}
