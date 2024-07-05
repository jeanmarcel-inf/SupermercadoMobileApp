package com.example.supermercadoapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _productData = mutableStateOf<List<Product>>(emptyList())
    val productData: State<List<Product>> = _productData

    init {
        viewModelScope.launch {
            getProducts()
        }
    }

    private suspend fun getProducts() {
        val productsList = RetrofitClient.productService.getProducts()
        _productData.value = productsList
    }

}