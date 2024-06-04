package com.example.prm1.data

import com.example.prm1.model.Product

interface ProductRepository {
    fun getProductList(): List<Product>
    fun add(product: Product)
    fun getProductById(id: Int): Product
    fun set(id: Int, product: Product)
    fun removeById(id: Int)
    fun getNextId(): Int
    fun getSize(): Int
}