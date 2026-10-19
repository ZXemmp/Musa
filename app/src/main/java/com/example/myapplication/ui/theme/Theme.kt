package com.example.myapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    // Violeta: identidad de la app
    primary = Violet,
    onPrimary = Color.White,
    primaryContainer = VioletContainer,
    onPrimaryContainer = OnVioletContainer,
    inversePrimary = VioletInDark,

    // Azul pavo real: acciones secundarias, chips, filtros
    secondary = Peacock,
    onSecondary = Color.White,
    secondaryContainer = PeacockContainer,
    onSecondaryContainer = OnPeacockContainer,

    // Terracota: llamadas de atención, exposiciones temporales
    tertiary = Terracotta,
    onTertiary = Color.White,
    tertiaryContainer = TerracottaContainer,
    onTertiaryContainer = OnTerracottaContainer,

    background = Ivory,
    onBackground = Ink,
    surface = Ivory,
    onSurface = Ink,
    surfaceVariant = LavenderMist,
    onSurfaceVariant = InkVariant,
    outline = OutlineLight,
    outlineVariant = OutlineSoft,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight
)

private val DarkColorScheme = darkColorScheme(
    primary = VioletInDark,
    onPrimary = OnVioletInDark,
    primaryContainer = VioletContainerDark,
    onPrimaryContainer = OnVioletContainerDark,
    inversePrimary = Violet,

    secondary = PeacockInDark,
    onSecondary = OnPeacockInDark,
    secondaryContainer = PeacockContainerDark,
    onSecondaryContainer = PeacockContainer,

    tertiary = TerracottaInDark,
    onTertiary = OnTerracottaInDark,
    tertiaryContainer = TerracottaContainerDark,
    onTertiaryContainer = TerracottaContainer,

    background = Aubergine,
    onBackground = NightText,
    surface = Aubergine,
    onSurface = NightText,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = NightTextVariant,
    outline = OutlineDark,
    outlineVariant = NightSurfaceVariant
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivado por defecto para conservar la paleta de la marca.
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