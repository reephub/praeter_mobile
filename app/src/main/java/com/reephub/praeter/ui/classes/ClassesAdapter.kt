package com.reephub.praeter.ui.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reephub.praeter.data.remote.dto.ClassesDto
import com.reephub.praeter.data.remote.dto.OrderItemDto
import com.reephub.praeter.databinding.RowClassBinding

class ClassesAdapter(val items: List<ClassesDto>) : RecyclerView.Adapter<RowClassesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowClassesViewHolder {
        val rowBinding =
            RowClassBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RowClassesViewHolder(rowBinding)
    }

    override fun onBindViewHolder(holder: RowClassesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}