package com.example.supermercadoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
fun ProductForm(viewModel: ProductViewModel = viewModel()) {
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
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

                // Formata a entrada de dados do usuário para o formato utilizado pela Api
                val productPriceFormat = productPrice.replace(',', '.')

                val product = Product(
                    name = productName,
                    price = productPriceFormat.toFloat(),
                    category = productCategory
                )
                viewModel.addProduct(product)

                // Limpar os campos ao enviar requisição
                productName = ""
                productPrice = ""
                productCategory = ""

                // Ocultar o teclado
                focusManager.clearFocus()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Adicionar Produto")
        }
    }
}

@Composable
fun ProductScreen(viewModel: ProductViewModel = viewModel()) {
    val productData by viewModel.productData.collectAsState()

    Column {
        Greetings()
        Spacer(modifier = Modifier.height(20.dp))

        ProductForm(viewModel = viewModel)
        Spacer(modifier = Modifier.height(30.dp))

        if(productData != emptyList<Product>()) {
            LazyRow {
                items(productData) { product ->
                    ProductItem(product)
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }
        }
        else {
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = "Não há itens cadastrados em estoque", fontSize = 20.sp, color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally))
        }

    }

}

@Composable
fun ProductItem(product: Product, viewModel: ProductViewModel = viewModel()) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("Id: ${product.productId}")
        Text("Nome: ${product.name}")
        Text("Preço: R$ ${String.format(Locale("pt", "BR"), "%.2f", product.price).replace('.', ',')}")
        Text("Categoria: ${product.category}")
        Button(onClick = {
            viewModel.deleteProduct(product.productId)
        }) {
            Text(text = "X")
        }
    }

}