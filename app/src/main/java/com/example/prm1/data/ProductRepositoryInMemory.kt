package com.example.prm1.data

import com.example.prm1.model.Product
import java.time.LocalDate

object ProductRepositoryInMemory : ProductRepository {
    private val productLists = mutableListOf(
        Product(1, "Pizza", "1", LocalDate.now().toString(), "Produkty Spożywcze"),
        Product(2, "Ibuprom", "2", LocalDate.of(2025, 1, 5).toString(), "Leki"),
        Product(3, "Krem nivea", null, LocalDate.now().toString(), "Kosmetyki"),
    )

    override fun getSize(): Int {
        return productLists.size
    }

    override fun getProductList(): List<Product> {
        productLists.sortBy { it.date }
        return productLists.toList()
    }

    override fun add(product: Product) {
        val modifiedProduct = if (product.count.isNullOrBlank()) {
            product.copy(count = null)
        } else {
            product
        }
        productLists.add(modifiedProduct)
        productLists.sortBy { it.date }
    }

    override fun getProductById(id: Int): Product {
        return productLists.find { it.id == id } ?: throw NoSuchElementException()
    }

    override fun set(id: Int, product: Product) {
        val existingProduct = getProductById(id)
        if (existingProduct.isExpired()) {
            return
        }
        if (!isValidProduct(product)) {
            return
        }
        val index = productLists.indexOfFirst { it.id == id }
        productLists[index] = product
        productLists.sortBy { it.date }
    }

    override fun removeById(id: Int) {
        productLists.removeIf { it.id == id }
        productLists.sortBy { it.date }
    }

    override fun getNextId(): Int {
        return productLists.maxOfOrNull { it.id }?.inc() ?: 1
    }

    private fun isValidProduct(product: Product): Boolean {
        return product.name.isNotBlank() && !product.isExpired() && isCountValid(product.count)
    }

    private fun isCountValid(count: String?): Boolean {
        if (count.isNullOrEmpty()) {
            return true
        }
        return try {
            count.toInt() >= 0
        } catch (e: NumberFormatException) {
            false
        }
    }
}