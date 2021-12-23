package com.reephub.praeter.ui.meetancients

import androidx.recyclerview.widget.RecyclerView
import com.reephub.praeter.data.remote.dto.AncientDto
import com.reephub.praeter.databinding.RowAncientBinding

class RowAncientViewHolder(val binding: RowAncientBinding) : RecyclerView.ViewHolder(binding.root) {

    private val viewBinding: RowAncientBinding get() = binding

    init {
    }

    fun bind(ancientItem: AncientDto) {
        viewBinding.ancient = ancientItem
    }
}
