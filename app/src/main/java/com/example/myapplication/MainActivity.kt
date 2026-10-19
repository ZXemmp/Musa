package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MistText
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.SheikahGradient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Barras del sistema con íconos claros, porque el tema de Musa es oscuro
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
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

object Routes {
    const val HOME = "inicio"
    const val EXHIBITIONS = "exhibiciones"
    const val VISIT = "mi_visita"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuseoApp() {

    var searchQuery by remember {
        mutableStateOf("")
    }

    // id de exhibición -> cantidad de entradas
    var cartQuantities by remember {
        mutableStateOf<Map<Int, Int>>(emptyMap())
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
        Category("\uD83C\uDFFA", "Historia y arqueología"),
        Category("\uD83C\uDFA8", "Arte y cultura"),
        Category("\uD83D\uDD2C", "Ciencia y tecnología"),
        Category("\uD83C\uDF3F", "Naturaleza y biodiversidad")
    )

    val filteredExhibiciones = exhibiciones.filter { exhibicion ->
        exhibicion.name.contains(
            searchQuery,
            ignoreCase = true
        )
    }

    // Las 4 exhibiciones mejor calificadas, para el inicio
    val destacadas = exhibiciones
        .sortedByDescending { it.rating }
        .take(4)

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(
                navController.graph.findStartDestination().id
            ) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
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
        topBar = {
            TopAppBar(
                title = {
                    Text("▲ Musa")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.secondary
                )
            )
        },
        bottomBar = {
            val itemColors = NavigationBarItemDefaults.colors(
                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                selectedTextColor = MaterialTheme.colorScheme.secondary,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                NavigationBarItem(
                    selected = currentRoute == Routes.HOME,
                    onClick = {
                        navigateTo(Routes.HOME)
                    },
                    icon = {
                        Text("\uD83C\uDFF0")
                    },
                    label = {
                        Text("Inicio")
                    },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.EXHIBITIONS,
                    onClick = {
                        navigateTo(Routes.EXHIBITIONS)
                    },
                    icon = {
                        Text("\uD83C\uDFFA")
                    },
                    label = {
                        Text("Exhibiciones")
                    },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.VISIT,
                    onClick = {
                        navigateTo(Routes.VISIT)
                    },
                    icon = {
                        Text("\uD83E\uDDED")
                    },
                    label = {
                        Text("Mi visita ($cartCount)")
                    },
                    colors = itemColors
                )
            }
        }


    ) { innerPading ->

        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPading)
        ) {

            // PANTALLA 1: INICIO

            composable(Routes.HOME) {
                HomeScreen(
                    categories = categories,
                    destacadas = destacadas,
                    onExplore = {
                        navigateTo(Routes.EXHIBITIONS)
                    }
                )
            }

            // PANTALLA 2: EXHIBICIONES

            composable(Routes.EXHIBITIONS) {
                ExhibicionesScreen(
                    searchQuery = searchQuery,
                    onSearchChange = { newText ->
                        searchQuery = newText
                    },
                    exhibiciones = filteredExhibiciones,
                    cartQuantities = cartQuantities,
                    onIncrease = { id ->
                        increaseQuantity(id)
                    },
                    onDecrease = { id ->
                        decreaseQuantity(id)
                    }
                )
            }

            // PANTALLA 3: MI VISITA

            composable(Routes.VISIT) {
                MiVisitaScreen(
                    exhibiciones = exhibiciones,
                    cartQuantities = cartQuantities,
                    onIncrease = { id ->
                        increaseQuantity(id)
                    },
                    onDecrease = { id ->
                        decreaseQuantity(id)
                    },
                    onExplore = {
                        navigateTo(Routes.EXHIBITIONS)
                    }
                )
            }
        }
    }
}

// =====================================================
//  PANTALLAS
// =====================================================

@Composable
fun HomeScreen(
    categories: List<Category>,
    destacadas: List<Exhibicion>,
    onExplore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        HeroBanner(onExplore = onExplore)

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Reinos del saber")

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryBadge(category = category)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OrnamentDivider()

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Reliquias más veneradas")

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destacadas) { exhibicion ->
                FeaturedCard(exhibicion = exhibicion)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ExhibicionesScreen(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    exhibiciones: List<Exhibicion>,
    cartQuantities: Map<Int, Int>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("Galería de reliquias")

        Text(
            text = "Explora cada sala y reserva tu entrada",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            label = { Text("Buscar exhibiciones") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (exhibiciones.isEmpty()) {
                item {
                    Text(
                        text = "Ninguna reliquia responde a tu búsqueda",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                items(exhibiciones) { exhibicion ->
                    val quantity = cartQuantities[exhibicion.id] ?: 0

                    ExhibicionCard(
                        exhibicion = exhibicion,
                        quantity = quantity,
                        onIncrease = {
                            onIncrease(exhibicion.id)
                        },
                        onDecrease = {
                            onDecrease(exhibicion.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MiVisitaScreen(
    exhibiciones: List<Exhibicion>,
    cartQuantities: Map<Int, Int>,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit,
    onExplore: () -> Unit
) {
    val seleccionadas = exhibiciones.filter { exhibicion ->
        cartQuantities.containsKey(exhibicion.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("Mi visita")

        Text(
            text = "Tu ruta por el santuario",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (seleccionadas.isEmpty()) {

            // Estado vacío
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\uD83E\uDDED",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tu ruta está vacía",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Agrega exhibiciones para planear tu recorrido.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onExplore) {
                        Text("Ver exhibiciones")
                    }
                }
            }

        } else {

            // Resumen de la visita
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Exhibiciones: ${seleccionadas.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Entradas: ${cartQuantities.values.sum()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(seleccionadas) { exhibicion ->
                    VisitaItem(
                        exhibicion = exhibicion,
                        quantity = cartQuantities[exhibicion.id] ?: 0,
                        onIncrease = {
                            onIncrease(exhibicion.id)
                        },
                        onDecrease = {
                            onDecrease(exhibicion.id)
                        }
                    )
                }
            }
        }
    }
}

// =====================================================
//  COMPONENTES REUTILIZABLES
// =====================================================

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun OrnamentDivider() {
    Text(
        text = "— ✦ —",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun HeroBanner(onExplore: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SheikahGradient)
            .border(1.dp, MaterialTheme.colorScheme.secondary, shape)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✦ Bienvenido, viajero ✦",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Descubre las reliquias del Caribe",
                style = MaterialTheme.typography.headlineSmall,
                color = MistText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Un santuario de historia, arte y naturaleza te espera.",
                style = MaterialTheme.typography.bodyMedium,
                color = MistText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onExplore) {
                Text("Explorar exhibiciones")
            }
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
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

/*
Si la cantidad es 0, Muestra agregar
Al presionar agregar, la cantidad pasa a 1
Cuando la cantidad es mayor que 0, aparece - y +
Al presionar +, la cantidad aumenta
Al presionar -, la cantidad disminuye
Cuando llega a 0, vuelve a aparecer agregar
En la barra inferior se ve la suma de las entradas de todas las exhibiciones
 */

@Composable
fun ExhibicionCard(
    exhibicion: Exhibicion,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
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
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(
                        text = "Nuevo",
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
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
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✦ ${exhibicion.sala}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (quantity == 0) {
                    Button(onClick = onIncrease) {
                        Text("Agregar")
                    }
                } else {
                    QuantitySelector(
                        quantity = quantity,
                        onIncrease = onIncrease,
                        onDecrease = onDecrease
                    )
                }
            }
        }
    }
}

@Composable
fun VisitaItem(
    exhibicion: Exhibicion,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = exhibicion.imagen),
                contentDescription = exhibicion.name,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exhibicion.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "✦ ${exhibicion.sala}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            QuantitySelector(
                quantity = quantity,
                onIncrease = onIncrease,
                onDecrease = onDecrease
            )
        }
    }
}

@Composable
fun FeaturedCard(exhibicion: Exhibicion) {
    Card(
        modifier = Modifier.width(220.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(id = exhibicion.imagen),
                contentDescription = exhibicion.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exhibicion.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "✦ ${exhibicion.sala}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "⭐ ${exhibicion.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun CategoryBadge(category: Category) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.emoji
            )
            Spacer(
                modifier = Modifier.width(6.dp)
            )
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
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