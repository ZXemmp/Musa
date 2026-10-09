package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MuseoApp()
                }
            }
        }
    }
}

data class Exhibicion(
    @DrawableRes val imagen: Int,
    val nombre: String,
    val descripcion: String,
    val sala: String
)

val exhibiciones = listOf(
    Exhibicion(
        imagen = R.drawable.precolombina,
        nombre = "Orfebrería Precolombina",
        descripcion = "Piezas de oro y cerámica de las culturas del Caribe colombiano.",
        sala = "Sala 1"
    ),
    Exhibicion(
        imagen = R.drawable.carnaval,
        nombre = "Memoria del Carnaval",
        descripcion = "Trajes, máscaras y fotografías históricas de la fiesta popular.",
        sala = "Sala 2"
    ),
    Exhibicion(
        imagen = R.drawable.artemoderno,
        nombre = "Arte Moderno Latinoamericano",
        descripcion = "Pintura y escultura del siglo XX de artistas de la región.",
        sala = "Sala 3"
    )
)

@Composable
fun MuseoApp() {
    var busqueda by remember { mutableStateOf("") }

    val resultados = exhibiciones.filter {
        it.nombre.contains(busqueda, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Musa",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Descubre nuestras colecciones",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar exhibiciones") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Exhibiciones Destacadas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        for (exhibicion in resultados) {
            ExhibicionCard(exhibicion)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ExhibicionCard(exhibicion: Exhibicion) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = exhibicion.imagen),
                contentDescription = exhibicion.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exhibicion.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = exhibicion.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = exhibicion.sala,
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = {}) {
                    Text("Ver más")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MuseoAppPreview() {
    MyApplicationTheme {
        MuseoApp()
    }
}