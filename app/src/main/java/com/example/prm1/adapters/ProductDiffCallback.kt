package com.example.prm1.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.prm1.model.Product

class ProductDiffCallback(
    private val old: List<Product>,
    private val new: List<Product>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] === new[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] == new[newItemPosition]
}