package com.example.prm1.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.example.prm1.R
import com.example.prm1.data.ProductRepository
import com.example.prm1.data.RepositoryLocator
import com.example.prm1.model.Product
import com.example.prm1.model.navigation.AddProduct
import com.example.prm1.model.navigation.Destination
import com.example.prm1.model.navigation.EditProduct

class ListViewModel : ViewModel() {
    private var productRepository: ProductRepository = RepositoryLocator.productRepository
    val products: MutableLiveData<List<Product>> = MutableLiveData(emptyList())
    val navigation = MutableLiveData<Destination>()
    val count = MutableLiveData(0)

    fun onAddDish() {
        navigation.value = AddProduct()
    }

    fun resetFilter() {
        loadProducts()
    }

    fun onProductEdit(id: Int) {
        navigation.value = EditProduct(id)
    }

    fun onProductRemove(id: Int) {
        productRepository.removeById(id)
        val productList = productRepository.getProductList()
        products.value = productList
        count.postValue(productList.size)
    }

    fun onDestinationChange(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination.id == R.id.listFragment) {
            loadProducts()
        }
    }

    private fun loadProducts() {
        products.value = productRepository.getProductList()
        count.value = productRepository.getSize()
    }
}
