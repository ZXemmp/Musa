package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
    @DrawableRes
    val imagen: Int,
    val name: String,
    val description: String,
    val rating: Double,
    val sala: String,
    val id: Int
)
data class Category(
    val emoji: String,
    val name: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuseoApp() {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var cartQuantities by remember {
        mutableStateOf<Map<Int, Int>>(emptyMap())
    }

    // NOTA: las exhibiciones 4 a 9 reutilizan las imágenes existentes.
    // Cámbialas por tus propias imágenes en res/drawable cuando las tengas.
    val exhibiciones = listOf(
        Exhibicion(
            imagen = R.drawable.precolombina,
            name = "Orfebrería Precolombina",
            description = "Piezas de oro y cerámica de las culturas del Caribe colombiano.",
            rating = 4.7,
            sala = "Sala 1",
            id = 1
        ),
        Exhibicion(
            imagen = R.drawable.carnaval,
            name = "Memoria del Carnaval",
            description = "Trajes, máscaras y fotografías históricas de la fiesta popular.",
            rating = 4.8,
            sala = "Sala 2",
            id = 2
        ),
        Exhibicion(
            imagen = R.drawable.artemoderno,
            name = "Arte Moderno Latinoamericano",
            description = "Pintura y escultura del siglo XX de artistas de la región.",
            rating = 4.4,
            sala = "Sala 3",
            id = 3
        ),
        Exhibicion(
            imagen = R.drawable.precolombina,
            name = "Cerámica Ancestral",
            description = "Vasijas y figuras elaboradas por comunidades indígenas hace siglos.",
            rating = 4.5,
            sala = "Sala 4",
            id = 4
        ),
        Exhibicion(
            imagen = R.drawable.carnaval,
            name = "Música y Tradición",
            description = "Instrumentos tradicionales y ritmos que marcaron la identidad cultural.",
            rating = 4.6,
            sala = "Sala 5",
            id = 5
        ),
        Exhibicion(
            imagen = R.drawable.artemoderno,
            name = "Fotografía Contemporánea",
            description = "Una mirada actual a la vida urbana a través de lentes locales.",
            rating = 4.3,
            sala = "Sala 6",
            id = 6
        ),
        Exhibicion(
            imagen = R.drawable.precolombina,
            name = "Biodiversidad del Caribe",
            description = "Especies de flora y fauna de los ecosistemas costeros y selváticos.",
            rating = 4.9,
            sala = "Sala 7",
            id = 7
        ),
        Exhibicion(
            imagen = R.drawable.carnaval,
            name = "Ciencia Interactiva",
            description = "Experimentos y módulos para descubrir la física y la tecnología.",
            rating = 4.2,
            sala = "Sala 8",
            id = 8
        ),
        Exhibicion(
            imagen = R.drawable.artemoderno,
            name = "Escultura al Aire Libre",
            description = "Obras monumentales de artistas nacionales en los jardines del museo.",
            rating = 4.6,
            sala = "Sala 9",
            id = 9
        )
    )

    val categories = listOf(
        Category("\uD83C\uDFFA","Historia y arqueología"),
        Category("\uD83C\uDFA8","Arte y cultura"),
        Category("\uD83D\uDD2C","Ciencia y tecnología"),
        Category("\uD83C\uDF3F","Naturaleza y biodiversidad")
    )

    val filteredExhibiciones = exhibiciones.filter { exhibicion ->
        exhibicion.name.contains(
            searchQuery,
            ignoreCase = true
        )
    }

    fun increaseQuantity(exhibicionId: Int) {
        val currentlyQuantity = cartQuantities[exhibicionId] ?: 0

        cartQuantities = cartQuantities + (
                exhibicionId to (currentlyQuantity + 1)
                )
    }

    fun decreaseQuantity(exhibicionId: Int) {
        val currentlyQuantity = cartQuantities[exhibicionId] ?: 0

        if (currentlyQuantity > 1) {
            cartQuantities = cartQuantities + (
                    exhibicionId to (currentlyQuantity - 1)
                    )
        } else {
            cartQuantities = cartQuantities - exhibicionId
        }
    }

    val cartCount = cartQuantities.values.sum()


    Scaffold(
        topBar ={
            TopAppBar(
                title = {
                    Text("\uD83C\uDFDB\uFE0F Musa")
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Text("🏠")
                    },
                    label = {
                        Text("Inicio")
                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Text("🛒")
                    },
                    label = {
                        Text("Carrito ($cartCount)")
                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Text("👤")
                    },
                    label = {
                        Text("Perfil")
                    }
                )
            }
        }


    ) { innerPading ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPading)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Hola!👋",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Descubre nuestras colecciones",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Busqueda

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { newText ->
                    searchQuery = newText
                },
                label = { Text("Buscar exhibiciones") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Categorías

            Text(
                text = "Categorías",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow (
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(categories){ category ->
                    CategoryBadge(
                        category = category
                    )
                }
            }

            //Exhibiciones

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Exhibiciones Destacadas",
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (filteredExhibiciones.isEmpty()){
                    item {
                        Text(
                            text = "No se encontraron Exhibiciones",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    items(filteredExhibiciones) { exhibicion ->
                        val quantity = cartQuantities[exhibicion.id] ?: 0

                        ExhibicionCard(
                            exhibicion = exhibicion,
                            quantity = quantity,
                            onIncrease = {
                                increaseQuantity(exhibicionId = exhibicion.id)
                            },
                            onDecrease = {
                                decreaseQuantity(exhibicionId = exhibicion.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

/*
Si la cantidad es 0, Muestra agregar
Al presionar agregar, la cantidad pasa a 1
Cuando la cantidad es mayor que 0, aparece - y +
Al presionar
 */

@Composable
fun ExhibicionCard(
    exhibicion: Exhibicion,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Image(
                    painter = painterResource(
                        id = exhibicion.imagen
                    ),
                    contentDescription = exhibicion.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Fit
                )

                Card(
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                ){
                    Text(
                        text = "Nuevo",
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exhibicion.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = exhibicion.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "⭐ ${exhibicion.rating}",
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
                if (quantity == 0) {
                    Button(onClick = onIncrease) {
                        Text("Agregar")
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDecrease,
                            contentPadding = PaddingValues(all = 0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("-")
                        }

                        Text(
                            text = quantity.toString(),
                            style = MaterialTheme.typography.titleMedium
                        )

                        OutlinedButton(
                            onClick = onIncrease,
                            contentPadding = PaddingValues(all = 0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("+")
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun CategoryBadge(category: Category){
    Card {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = category.emoji
            )
            Spacer(
                modifier = Modifier.width(6.dp)
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge
            )
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