package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// =====================================================
//  MUSA · Santuario de las reliquias
//  Paleta mística: azul Sheikah, oro Trifuerza y verde Kokiri
// =====================================================

// ---------- Dark (identidad principal: templo de noche) ----------
val SheikahCyan = Color(0xFF4FD1E8)           // brillo azul de las runas
val OnSheikahCyan = Color(0xFF00363D)
val SheikahContainerDark = Color(0xFF0E4A55)
val OnSheikahContainerDark = Color(0xFFC2F4FB)

val TriforceGold = Color(0xFFE6C25A)          // oro antiguo
val TriforceGoldBright = Color(0xFFF6DC8B)
val OnTriforceGold = Color(0xFF3A2D00)
val GoldContainerDark = Color(0xFF5A4500)
val OnGoldContainerDark = Color(0xFFFFE9A8)

val KorokGreen = Color(0xFF8FD694)            // bosque sagrado
val OnKorokGreen = Color(0xFF0A3814)
val KorokContainerDark = Color(0xFF1F5130)
val OnKorokContainerDark = Color(0xFFB0F2B5)

val TempleNight = Color(0xFF0B1418)           // fondo: piedra de templo
val TempleStone = Color(0xFF16292E)           // tarjetas
val MistText = Color(0xFFE8EFE6)
val MistTextVariant = Color(0xFFB5C9CC)
val OutlineDark = Color(0xFF6F8F94)
val OutlineDarkSoft = Color(0xFF2C4A50)

// ---------- Light (pergamino) ----------
val DeepTeal = Color(0xFF0B6B7A)
val TealContainer = Color(0xFFB8EDF5)
val OnTealContainer = Color(0xFF00252B)

val AncientGold = Color(0xFF8A6A00)
val GoldContainer = Color(0xFFFFE08A)
val OnGoldContainer = Color(0xFF2B2000)

val Forest = Color(0xFF2E6B3A)
val ForestContainer = Color(0xFFB5F0BA)
val OnForestContainer = Color(0xFF002108)

val Parchment = Color(0xFFF6EFD9)
val ParchmentSurface = Color(0xFFFBF6E6)
val ParchmentVariant = Color(0xFFE9DFC0)
val InkGreen = Color(0xFF1E2A2C)
val InkGreenVariant = Color(0xFF4A5A55)
val OutlineLight = Color(0xFF7A8A80)
val OutlineLightSoft = Color(0xFFCFC49E)

// ---------- Degradados ----------
val SheikahGradient = Brush.verticalGradient(
    listOf(Color(0xFF0E4A55), Color(0xFF0B2A33), TempleNight)
)
val GoldGradient = Brush.linearGradient(listOf(TriforceGold, TriforceGoldBright))