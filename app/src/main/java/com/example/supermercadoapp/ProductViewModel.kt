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
        viewModelScope.launch {
            getProducts()
        }
    }

    suspend fun getProducts() {
        val productsList = RetrofitClient.productService.getProducts()
        _productData.value = productsList
    }

}