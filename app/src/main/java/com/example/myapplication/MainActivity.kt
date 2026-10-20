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
import androidx.compose.foundation.clickable
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

// =====================================================
//  MODELOS DE DATOS
// =====================================================

// Una obra del museo con toda la información para acompañar al visitante
data class Obra(
    @DrawableRes
    val imagen: Int,
    val titulo: String,
    val autor: String,
    val epoca: String,
    val tecnica: String,
    val categoria: String,
    val contexto: String,
    val curiosidad: String,
    val sala: String,
    val id: Int
)

data class Category(
    val emoji: String,
    val name: String
)

// Un turno de visita con cupos disponibles
data class Horario(
    val hora: String,
    val cupos: Int
)

object Routes {
    const val HOME = "inicio"
    const val EXPLORE = "explorar"
    const val FAVORITES = "favoritas"
    const val BOOKING = "reservar"
    const val DETAIL = "detalle"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuseoApp() {

    // ---------- ESTADO ----------

    var searchQuery by remember {
        mutableStateOf("")
    }

    // null = todas las categorías
    var categoriaSeleccionada by remember {
        mutableStateOf<String?>(null)
    }

    // ids de las obras marcadas como favoritas
    var favoritos by remember {
        mutableStateOf<Set<Int>>(emptySet())
    }

    // obra que se está viendo en la pantalla de detalle
    var obraSeleccionadaId by remember {
        mutableStateOf<Int?>(null)
    }

    // reserva
    var horarioSeleccionado by remember {
        mutableStateOf<String?>(null)
    }

    var visitantes by remember {
        mutableStateOf(1)
    }

    var reservaConfirmada by remember {
        mutableStateOf(false)
    }

    // ---------- NAVEGACIÓN ----------

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ---------- DATOS ----------

    val obras = listOf(
        Obra(
            imagen = R.drawable.noche_estrellada,
            titulo = "La noche estrellada",
            autor = "Vincent van Gogh",
            epoca = "1889 · Postimpresionismo",
            tecnica = "Óleo sobre lienzo",
            categoria = "Pintura",
            contexto = "Van Gogh la pintó en junio de 1889, mientras estaba internado voluntariamente en un asilo de Saint-Rémy-de-Provence, en Francia. La vista parte de la ventana de su habitación, pero el cielo en espiral y el pueblo nacen de su memoria y de su emoción más que de la realidad.",
            curiosidad = "El pueblo del primer plano no existía tal como aparece: Van Gogh lo inventó. Y aunque el cuadro es nocturno, lo pintó de día, en su cuarto de trabajo, recordando el cielo.",
            sala = "Sala 1",
            id = 1
        ),
        Obra(
            imagen = R.drawable.gioconda,
            titulo = "La Gioconda (Mona Lisa)",
            autor = "Leonardo da Vinci",
            epoca = "c. 1503–1519 · Renacimiento italiano",
            tecnica = "Óleo sobre tabla de álamo",
            categoria = "Pintura",
            contexto = "Se cree que retrata a Lisa Gherardini, esposa de un comerciante de seda de Florencia. Leonardo trabajó en ella durante años y la llevó consigo a Francia, donde hoy forma parte de las colecciones del Museo del Louvre.",
            curiosidad = "Fue robada en 1911 y estuvo desaparecida más de dos años; el escándalo la hizo famosa en todo el mundo. Su sonrisa difusa se logra con el sfumato: capas finísimas de pintura sin contornos marcados.",
            sala = "Sala 1",
            id = 2
        ),
        Obra(
            imagen = R.drawable.el_pensador,
            titulo = "El pensador",
            autor = "Auguste Rodin",
            epoca = "Concebida en 1880 · Escultura moderna",
            tecnica = "Bronce fundido",
            categoria = "Escultura",
            contexto = "Rodin la imaginó como la figura central de La puerta del Infierno, un portal inspirado en La divina comedia de Dante. Al principio representaba al poeta meditando frente al abismo; con el tiempo se convirtió en una obra independiente.",
            curiosidad = "Existen numerosos vaciados en bronce en museos y plazas de todo el mundo, así que una misma obra puede estar en muchos lugares a la vez.",
            sala = "Sala 2",
            id = 3
        ),
        Obra(
            imagen = R.drawable.venus_de_milo,
            titulo = "Venus de Milo",
            autor = "Alejandro de Antioquía (atribuida)",
            epoca = "c. 130–100 a. C. · Período helenístico",
            tecnica = "Mármol de Paros",
            categoria = "Escultura",
            contexto = "Fue hallada en 1820 por un campesino en la isla griega de Milos. Poco después llegó a Francia y desde entonces se exhibe en el Louvre. Probablemente representa a Afrodita, la diosa del amor.",
            curiosidad = "Nunca se encontraron sus brazos. Hay muchas teorías sobre qué sostenían, como una manzana o su propio vestido, pero sigue siendo un misterio.",
            sala = "Sala 2",
            id = 4
        ),
        Obra(
            imagen = R.drawable.mascara_tutankamon,
            titulo = "Máscara de Tutankamón",
            autor = "Artesanos del antiguo Egipto",
            epoca = "c. 1323 a. C. · Reino Nuevo",
            tecnica = "Oro con incrustaciones de lapislázuli y otras piedras",
            categoria = "Arqueología",
            contexto = "Cubría el rostro de la momia del joven faraón Tutankamón. Su tumba, en el Valle de los Reyes, fue descubierta en 1922 por el arqueólogo Howard Carter y es uno de los hallazgos más importantes de la egiptología.",
            curiosidad = "Pesa unos 10 kilos y la tumba que la protegía permaneció casi intacta durante más de 3.000 años.",
            sala = "Sala 3",
            id = 5
        ),
        Obra(
            imagen = R.drawable.poporo_quimbaya,
            titulo = "Poporo Quimbaya",
            autor = "Orfebres de la cultura Quimbaya",
            epoca = "c. 500–1000 d. C. · Colombia precolombina",
            tecnica = "Oro fundido a la cera perdida",
            categoria = "Arqueología",
            contexto = "Los quimbayas habitaron la región central de Colombia, en el valle del río Cauca. El poporo era un recipiente para guardar la cal que se mezclaba con hojas de coca en ceremonias y rituales, por eso se fabricaba con tanto cuidado.",
            curiosidad = "Se hacía con la técnica de la cera perdida: el orfebre modelaba la pieza en cera, la cubría de arcilla y, al calentarla, el oro fundido ocupaba el lugar de la cera.",
            sala = "Sala 3",
            id = 6
        )
    )

    val categories = listOf(
        Category("\uD83C\uDFA8", "Pintura"),
        Category("\uD83D\uDDFF", "Escultura"),
        Category("\uD83C\uDFFA", "Arqueología"),
        Category("\uD83C\uDFAD", "Cultura popular")
    )

    val horarios = listOf(
        Horario("9:00 a. m.", 12),
        Horario("10:30 a. m.", 8),
        Horario("12:00 m.", 0),
        Horario("2:00 p. m.", 15),
        Horario("3:30 p. m.", 6),
        Horario("5:00 p. m.", 10)
    )

    // Curiosidad que se muestra en el inicio
    val obraDelDia = remember { obras.random() }

    // Búsqueda por título o autor + filtro por categoría
    val obrasFiltradas = obras.filter { obra ->
        val coincideBusqueda =
            obra.titulo.contains(searchQuery, ignoreCase = true) ||
                    obra.autor.contains(searchQuery, ignoreCase = true)

        val coincideCategoria =
            categoriaSeleccionada == null || obra.categoria == categoriaSeleccionada

        coincideBusqueda && coincideCategoria
    }

    val obrasFavoritas = obras.filter { obra ->
        obra.id in favoritos
    }

    // ---------- FUNCIONES ----------

    fun navigateTo(route: String) {
        // Si estamos en una ficha, primero la cerramos
        if (currentRoute == Routes.DETAIL) {
            navController.popBackStack()
        }

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

    fun openObra(obraId: Int) {
        obraSeleccionadaId = obraId
        navController.navigate(Routes.DETAIL) {
            launchSingleTop = true
        }
    }

    fun toggleFavorito(obraId: Int) {
        favoritos = if (obraId in favoritos) {
            favoritos - obraId
        } else {
            favoritos + obraId
        }
    }

    fun seleccionarHorario(hora: String) {
        horarioSeleccionado = hora

        // Si ya había más visitantes que cupos, se ajusta
        val cupos = horarios.first { it.hora == hora }.cupos
        if (visitantes > cupos) {
            visitantes = cupos
        }
    }

    fun aumentarVisitantes() {
        val cupos = horarios.find { it.hora == horarioSeleccionado }?.cupos ?: 0
        if (visitantes < cupos) {
            visitantes += 1
        }
    }

    fun disminuirVisitantes() {
        if (visitantes > 1) {
            visitantes -= 1
        }
    }

    fun cancelarReserva() {
        reservaConfirmada = false
        horarioSeleccionado = null
        visitantes = 1
    }

    // ---------- INTERFAZ ----------

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
                    selected = currentRoute == Routes.EXPLORE,
                    onClick = {
                        navigateTo(Routes.EXPLORE)
                    },
                    icon = {
                        Text("\uD83C\uDFFA")
                    },
                    label = {
                        Text("Explorar")
                    },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.FAVORITES,
                    onClick = {
                        navigateTo(Routes.FAVORITES)
                    },
                    icon = {
                        Text("\uD83D\uDC8E")
                    },
                    label = {
                        Text("Favoritas (${favoritos.size})")
                    },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Routes.BOOKING,
                    onClick = {
                        navigateTo(Routes.BOOKING)
                    },
                    icon = {
                        Text("\uD83C\uDF9F\uFE0F")
                    },
                    label = {
                        Text("Reservar")
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
                    obras = obras,
                    obraDelDia = obraDelDia,
                    horarioReservado = if (reservaConfirmada) horarioSeleccionado else null,
                    visitantes = visitantes,
                    onExplore = {
                        navigateTo(Routes.EXPLORE)
                    },
                    onCategoryClick = { nombre ->
                        categoriaSeleccionada = nombre
                        navigateTo(Routes.EXPLORE)
                    },
                    onOpenObra = { id ->
                        openObra(id)
                    },
                    onViewBooking = {
                        navigateTo(Routes.BOOKING)
                    }
                )
            }

            // PANTALLA 2: EXPLORAR (búsqueda + filtro por categoría)
            composable(Routes.EXPLORE) {
                ExplorarScreen(
                    searchQuery = searchQuery,
                    onSearchChange = { newText ->
                        searchQuery = newText
                    },
                    categories = categories,
                    categoriaSeleccionada = categoriaSeleccionada,
                    onCategorySelect = { nombre ->
                        categoriaSeleccionada = nombre
                    },
                    obras = obrasFiltradas,
                    favoritos = favoritos,
                    onToggleFavorite = { id ->
                        toggleFavorito(id)
                    },
                    onOpenObra = { id ->
                        openObra(id)
                    }
                )
            }

            // PANTALLA 3: FAVORITAS
            composable(Routes.FAVORITES) {
                FavoritasScreen(
                    obras = obrasFavoritas,
                    onToggleFavorite = { id ->
                        toggleFavorito(id)
                    },
                    onOpenObra = { id ->
                        openObra(id)
                    },
                    onExplore = {
                        navigateTo(Routes.EXPLORE)
                    }
                )
            }

            // PANTALLA 4: RESERVAR
            composable(Routes.BOOKING) {
                ReservarScreen(
                    horarios = horarios,
                    horarioSeleccionado = horarioSeleccionado,
                    visitantes = visitantes,
                    reservaConfirmada = reservaConfirmada,
                    onSelectHorario = { hora ->
                        seleccionarHorario(hora)
                    },
                    onIncrease = {
                        aumentarVisitantes()
                    },
                    onDecrease = {
                        disminuirVisitantes()
                    },
                    onConfirm = {
                        reservaConfirmada = true
                    },
                    onCancel = {
                        cancelarReserva()
                    }
                )
            }

            // PANTALLA 5: FICHA DE LA OBRA
            composable(Routes.DETAIL) {
                val obra = obras.find { it.id == obraSeleccionadaId }

                DetalleObraScreen(
                    obra = obra,
                    isFavorite = obra != null && obra.id in favoritos,
                    onToggleFavorite = {
                        if (obra != null) {
                            toggleFavorito(obra.id)
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    },
                    onReservar = {
                        navigateTo(Routes.BOOKING)
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
    obras: List<Obra>,
    obraDelDia: Obra,
    horarioReservado: String?,
    visitantes: Int,
    onExplore: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onOpenObra: (Int) -> Unit,
    onViewBooking: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        HeroBanner(onExplore = onExplore)

        // Si hay reserva confirmada, se muestra aquí
        if (horarioReservado != null) {
            Spacer(modifier = Modifier.height(16.dp))

            ReservaBanner(
                hora = horarioReservado,
                visitantes = visitantes,
                onClick = onViewBooking
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Curiosidad del día")

        Spacer(modifier = Modifier.height(10.dp))

        CuriosidadCard(
            obra = obraDelDia,
            onOpen = {
                onOpenObra(obraDelDia.id)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SectionTitle("Descubre por categoría")

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                CategoryBadge(
                    category = category,
                    onClick = {
                        onCategoryClick(category.name)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OrnamentDivider()

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("Obras para descubrir")

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(obras) { obra ->
                FeaturedCard(
                    obra = obra,
                    onClick = {
                        onOpenObra(obra.id)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ExplorarScreen(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    categories: List<Category>,
    categoriaSeleccionada: String?,
    onCategorySelect: (String?) -> Unit,
    obras: List<Obra>,
    favoritos: Set<Int>,
    onToggleFavorite: (Int) -> Unit,
    onOpenObra: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("Explora las obras")

        Text(
            text = "Descubre por título, autor o categoría",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            label = { Text("Buscar por obra o autor") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filtro por categorías
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryBadge(
                    category = Category("✦", "Todas"),
                    selected = categoriaSeleccionada == null,
                    onClick = {
                        onCategorySelect(null)
                    }
                )
            }

            items(categories) { category ->
                val selected = categoriaSeleccionada == category.name

                CategoryBadge(
                    category = category,
                    selected = selected,
                    onClick = {
                        // Si ya estaba seleccionada, se quita el filtro
                        onCategorySelect(if (selected) null else category.name)
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (obras.isEmpty()) {
                item {
                    Text(
                        text = "Ninguna obra responde a tu búsqueda",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                items(obras) { obra ->
                    ObraCard(
                        obra = obra,
                        isFavorite = obra.id in favoritos,
                        onToggleFavorite = {
                            onToggleFavorite(obra.id)
                        },
                        onOpen = {
                            onOpenObra(obra.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritasScreen(
    obras: List<Obra>,
    onToggleFavorite: (Int) -> Unit,
    onOpenObra: (Int) -> Unit,
    onExplore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("Reliquias favoritas")

        Text(
            text = "Las obras que quieres volver a ver",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (obras.isEmpty()) {

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
                        text = "\uD83D\uDC8E",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Aún no tienes favoritas",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Toca el ♡ en una obra para guardarla aquí.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onExplore) {
                        Text("Explorar obras")
                    }
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(obras) { obra ->
                    ObraCard(
                        obra = obra,
                        isFavorite = true,
                        onToggleFavorite = {
                            onToggleFavorite(obra.id)
                        },
                        onOpen = {
                            onOpenObra(obra.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReservarScreen(
    horarios: List<Horario>,
    horarioSeleccionado: String?,
    visitantes: Int,
    reservaConfirmada: Boolean,
    onSelectHorario: (String) -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val cuposMaximos = horarios.find { it.hora == horarioSeleccionado }?.cupos

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        item {
            SectionTitle("Reserva tu visita")
            Text(
                text = "Elige un horario y cuántas personas asistirán",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (reservaConfirmada) {

            item {
                TicketCard(
                    hora = horarioSeleccionado ?: "",
                    visitantes = visitantes
                )
            }

            item {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar reserva")
                }
            }

        } else {

            items(horarios) { horario ->
                HorarioCard(
                    horario = horario,
                    selected = horario.hora == horarioSeleccionado,
                    onClick = {
                        onSelectHorario(horario.hora)
                    }
                )
            }

            item {
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
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Visitantes",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = if (cuposMaximos != null) {
                                    "Máximo $cuposMaximos en este horario"
                                } else {
                                    "Elige primero un horario"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        QuantitySelector(
                            quantity = visitantes,
                            onIncrease = onIncrease,
                            onDecrease = onDecrease
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = onConfirm,
                    enabled = horarioSeleccionado != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar reserva")
                }
            }
        }
    }
}

@Composable
fun DetalleObraScreen(
    obra: Obra?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit,
    onReservar: () -> Unit
) {
    if (obra == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("No se encontró la obra")
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onBack) {
                Text("← Volver")
            }
        }
    } else {
        val shape = RoundedCornerShape(16.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("← Volver")
                }

                FavoriteButton(
                    isFavorite = isFavorite,
                    onToggle = onToggleFavorite
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fotografía de la obra
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondary,
                        shape
                    )
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = obra.imagen),
                    contentDescription = obra.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = obra.titulo,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Ficha técnica
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
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(label = "Autor", value = obra.autor)
                    InfoRow(label = "Época", value = obra.epoca)
                    InfoRow(label = "Técnica", value = obra.tecnica)
                    InfoRow(label = "Categoría", value = obra.categoria)
                    InfoRow(label = "Ubicación", value = obra.sala)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionTitle("Contexto histórico")

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = obra.contexto,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Curiosidad
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "✦ ¿Sabías que...?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = obra.curiosidad,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onReservar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reservar mi visita")
            }

            Spacer(modifier = Modifier.height(24.dp))
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
                text = "Tu guía mística por el museo",
                style = MaterialTheme.typography.headlineSmall,
                color = MistText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Descubre cada obra, conoce su historia y planea tu recorrido.",
                style = MaterialTheme.typography.bodyMedium,
                color = MistText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onExplore) {
                Text("Explorar obras")
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

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit
) {
    OutlinedButton(
        onClick = onToggle,
        contentPadding = PaddingValues(all = 0.dp),
        modifier = Modifier.size(40.dp)
    ) {
        Text(
            text = if (isFavorite) "♥" else "♡",
            color = if (isFavorite) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.width(88.dp),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ObraCard(
    obra: Obra,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
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
                    .height(160.dp)
            ) {
                Image(
                    painter = painterResource(id = obra.imagen),
                    contentDescription = obra.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
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
                        text = obra.categoria,
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
                text = obra.titulo,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = obra.autor,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = obra.epoca,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✦ ${obra.sala}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FavoriteButton(
                        isFavorite = isFavorite,
                        onToggle = onToggleFavorite
                    )

                    Button(onClick = onOpen) {
                        Text("Ver ficha")
                    }
                }
            }
        }
    }
}

@Composable
fun FeaturedCard(
    obra: Obra,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick),
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
                painter = painterResource(id = obra.imagen),
                contentDescription = obra.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = obra.titulo,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2
            )

            Text(
                text = obra.autor,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )

            Text(
                text = "✦ ${obra.sala}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun CuriosidadCard(
    obra: Obra,
    onOpen: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "✦ ${obra.titulo}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = obra.curiosidad,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = onOpen) {
                Text("Ver ficha completa")
            }
        }
    }
}

@Composable
fun CategoryBadge(
    category: Category,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        ),
        border = if (selected) {
            BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
        } else {
            null
        }
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
                color = if (selected) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }
            )
        }
    }
}

@Composable
fun HorarioCard(
    horario: Horario,
    selected: Boolean,
    onClick: () -> Unit
) {
    val agotado = horario.cupos == 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !agotado, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            MaterialTheme.colorScheme.secondary.copy(
                alpha = if (selected) 1f else 0.5f
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\uD83D\uDD52 ${horario.hora}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = if (agotado) "Agotado" else "${horario.cupos} cupos",
                style = MaterialTheme.typography.labelLarge,
                color = if (agotado) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.tertiary
                }
            )
        }
    }
}

@Composable
fun TicketCard(
    hora: String,
    visitantes: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✦ Reserva confirmada ✦",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Horario: $hora",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Visitantes: $visitantes",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Presenta este pase en la entrada del museo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ReservaBanner(
    hora: String,
    visitantes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\uD83C\uDF9F\uFE0F Tu visita: $hora",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "$visitantes pers.",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
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