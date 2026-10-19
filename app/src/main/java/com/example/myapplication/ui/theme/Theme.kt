package com.example.myapplication.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    // Azul profundo: acciones principales
    primary = DeepTeal,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = OnTealContainer,

    // Oro antiguo: títulos, detalles y calificaciones
    secondary = AncientGold,
    onSecondary = Color.White,
    secondaryContainer = GoldContainer,
    onSecondaryContainer = OnGoldContainer,

    // Verde bosque: naturaleza y etiquetas
    tertiary = Forest,
    onTertiary = Color.White,
    tertiaryContainer = ForestContainer,
    onTertiaryContainer = OnForestContainer,

    background = Parchment,
    onBackground = InkGreen,
    surface = ParchmentSurface,
    onSurface = InkGreen,
    surfaceVariant = ParchmentVariant,
    onSurfaceVariant = InkGreenVariant,
    outline = OutlineLight,
    outlineVariant = OutlineLightSoft
)

private val DarkColorScheme = darkColorScheme(
    // Azul Sheikah: acciones principales
    primary = SheikahCyan,
    onPrimary = OnSheikahCyan,
    primaryContainer = SheikahContainerDark,
    onPrimaryContainer = OnSheikahContainerDark,

    // Oro Trifuerza: títulos, detalles y calificaciones
    secondary = TriforceGold,
    onSecondary = OnTriforceGold,
    secondaryContainer = GoldContainerDark,
    onSecondaryContainer = OnGoldContainerDark,

    // Verde Kokiri: naturaleza y etiquetas
    tertiary = KorokGreen,
    onTertiary = OnKorokGreen,
    tertiaryContainer = KorokContainerDark,
    onTertiaryContainer = OnKorokContainerDark,

    background = TempleNight,
    onBackground = MistText,
    surface = TempleNight,
    onSurface = MistText,
    surfaceVariant = TempleStone,
    onSurfaceVariant = MistTextVariant,
    outline = OutlineDark,
    outlineVariant = OutlineDarkSoft
)

@Composable
fun MyApplicationTheme(
    // La identidad de Musa es "templo de noche": se fuerza el tema oscuro.
    // Para seguir el tema del teléfono usa: isSystemInDarkTheme()
    darkTheme: Boolean = true,
    // Desactivado para conservar la paleta de la marca.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}