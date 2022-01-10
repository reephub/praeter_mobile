package com.reephub.praeter.ui.classes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reephub.praeter.data.remote.dto.ClassesDto
import com.reephub.praeter.databinding.RowClassBinding

class ClassesAdapter(
    val items: List<ClassesDto>,
    private val mListener: ClassClickListener
) : RecyclerView.Adapter<RowClassesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowClassesViewHolder {
        val rowBinding =
            RowClassBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RowClassesViewHolder(rowBinding, mListener)
    }

    override fun onBindViewHolder(holder: RowClassesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}