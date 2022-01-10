package com.reephub.praeter.ui.classes

import androidx.recyclerview.widget.RecyclerView
import com.reephub.praeter.data.remote.dto.ClassesDto
import com.reephub.praeter.databinding.RowClassBinding

class RowClassesViewHolder(val binding: RowClassBinding, val listener: ClassClickListener) :
    RecyclerView.ViewHolder(binding.root) {

    private val viewBinding: RowClassBinding get() = binding

    init {
        run {
            viewBinding.mListener = listener
        }
    }

    fun bind(classItem: ClassesDto) {
        viewBinding.classItem = classItem
    }
}