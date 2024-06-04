package com.example.prm1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.prm1.databinding.ItemProductBinding
import com.example.prm1.model.Product

class ProductItem(private val itemViewBinding: ItemProductBinding) :
    RecyclerView.ViewHolder(itemViewBinding.root) {
    var id: Int = 0
        private set

    fun onBind(productItem: Product, onItemClick: () -> Unit, onItemLongClick: () -> Unit) {
        with(itemViewBinding) {
            id = productItem.id
            title.text = productItem.name
            subtitle.text = productItem.date
            category.text = productItem.category
            count.text = productItem.count.toString()
            root.setOnClickListener {
                if (productItem.isExpired()) {
                    Toast.makeText(
                        root.context,
                        "Produkt jest przeterminowany!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onItemClick()
                }
            }
            root.setOnLongClickListener {
                if (productItem.isExpired()) {
                    Toast.makeText(
                        root.context,
                        "Produkt jest przeterminowany!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onItemLongClick()
                }
                return@setOnLongClickListener true
            }
        }
    }
}

class ProductListAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Int) -> Unit
) : RecyclerView.Adapter<ProductItem>() {
    var productList: List<Product> = emptyList()
        set(value) {
            val diffs = DiffUtil.calculateDiff(ProductDiffCallback(field, value))
            field = value
            diffs.dispatchUpdatesTo(this)
        }

    fun updateList(products: List<Product>) {
        productList = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItem {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductItem(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductItem, position: Int) {
        holder.onBind(
            productList[position],
            onItemClick = { onItemClick(position) },
            onItemLongClick = { onItemLongClick(position) })
    }
}