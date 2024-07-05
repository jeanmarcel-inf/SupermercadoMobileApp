package com.example.supermercadoapp.network

import com.example.supermercadoapp.Product
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {

    @GET("/products")
    suspend fun getProducts(): List<Product>

    // Talvez seja necessário utilizar headers
    @POST("/products")
    suspend fun addProduct(@Body product: Product): Product
}