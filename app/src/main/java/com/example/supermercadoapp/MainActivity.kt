package com.example.supermercadoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.supermercadoapp.ui.theme.SupermercadoAppTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupermercadoAppTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ProductData()
                    ProductScreen()
                }
            }
        }
    }
}

@Composable
fun Greetings() {
    Column (modifier = Modifier.padding(10.dp), ) {
        Text(text = "Bem vindo ao mercado Kotlin!", fontSize = 40.sp, lineHeight = 40.sp)
        Spacer(modifier = Modifier.size(14.dp))
        Text(text = "Utilize os campos abaixo para adicionar produtos ao estoque.", fontSize = 18.sp, color = Color.Gray)
    }

}

@Composable
fun ProductScreen() {
    var products by remember { mutableStateOf(listOf<Product>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Greetings()
        Spacer(modifier = Modifier.height(20.dp))
        ProductForm { product ->
            products = products + product
        }
        Spacer(modifier = Modifier.height(20.dp))
        ProductList()
    }
}

@Composable
fun ProductForm(onProductAdded: (Product) -> Unit) {
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") }

    Column {
        TextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = productCategory,
            onValueChange = { productCategory = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val price = productPrice.replace(',', '.').toFloatOrNull()
                if (productName.isNotBlank() && price != null && productCategory.isNotBlank()) {
                    val product = Product(productName, price, productCategory)
                    onProductAdded(product)
                    productName = ""
                    productPrice = ""
                    productCategory = ""
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Enviar")
        }
    }
}

@Composable
fun ProductList(viewModel: ProductViewModel = viewModel()) {
    val productData by viewModel.productData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getProducts()
    }

    LazyColumn {
        items(productData) { product ->
            ProductItem(product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Nome: ${product.name}")
        Text("Preço: R$ ${String.format(Locale("pt", "BR"), "%.2f", product.price).replace('.', ',')}")
        Text("Categoria: ${product.category}")
    }
}