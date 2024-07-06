package com.example.supermercadoapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _productData = MutableStateFlow<List<Product>>(emptyList())
    val productData: StateFlow<List<Product>> get() = _productData

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            try {
                val productsList = RetrofitClient.productService.getProducts()
                _productData.value = productsList
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            try {
                val newProduct = RetrofitClient.productService.addProduct(product)

                _productData.value += newProduct
            } catch (e: Exception) {
                // Handle the exception
            }
        }

    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            try {
                RetrofitClient.productService.deleteProduct(productId)
                _productData.value = _productData.value.filterNot { it.productId == productId }
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }

}