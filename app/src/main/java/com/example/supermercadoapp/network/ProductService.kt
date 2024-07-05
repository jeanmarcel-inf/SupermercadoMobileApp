package com.example.supermercadoapp.network

import com.example.supermercadoapp.Product
import retrofit2.http.GET

interface ProductService {

    @GET("/products")
    suspend fun getProducts(): List<Product>
}