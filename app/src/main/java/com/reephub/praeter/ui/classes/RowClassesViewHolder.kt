package com.reephub.praeter.ui.classes

import androidx.recyclerview.widget.RecyclerView
import com.reephub.praeter.data.remote.dto.OrderItemDto
import com.reephub.praeter.databinding.RowClassBinding

class RowClassesViewHolder(val binding: RowClassBinding) : RecyclerView.ViewHolder(binding.root) {

    private val viewBinding: RowClassBinding get() = binding

    init {
    }

    fun bind(classItem: OrderItemDto) {
        viewBinding.orderItem = classItem
    }
}