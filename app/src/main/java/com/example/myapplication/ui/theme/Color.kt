package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ---------- Marca: Violeta amatista ----------
val Violet = Color(0xFF9600FF)        // Alpha, Red, Green, Blue
val VioletDark = Color(0xFF7000BD)
val VioletLight = Color(0xFFBA61FF)
val BackGround = Color(0xFFFFFFFF)

// ---------- Paleta "joyas de museo" (tonos intensos pero sobrios) ----------
val Magenta = Color(0xFFC2185B)       // granate vivo, para destacar
val Marigold = Color(0xFFE0A526)      // dorado cálido: favoritos, estrellas, "destacado"
val MarigoldBright = Color(0xFFF2C14E)

// ---------- Light ----------
val VioletContainer = Color(0xFFEBD6FF)
val OnVioletContainer = Color(0xFF2B0050)

val Peacock = Color(0xFF007C89)       // azul pavo real
val PeacockContainer = Color(0xFFB5EEF5)
val OnPeacockContainer = Color(0xFF001F25)

val Terracotta = Color(0xFFB4381F)    // cerámica / arcilla
val TerracottaContainer = Color(0xFFFFDBD1)
val OnTerracottaContainer = Color(0xFF3A0A00)

val Ivory = Color(0xFFFBF7FF)         // fondo marfil con un toque lavanda
val Ink = Color(0xFF1B1620)
val LavenderMist = Color(0xFFEFE6F7)  // superficies de tarjetas
val InkVariant = Color(0xFF4B4256)
val OutlineLight = Color(0xFF7D7387)
val OutlineSoft = Color(0xFFCFC3DA)
val InverseSurfaceLight = Color(0xFF312B38)
val InverseOnSurfaceLight = Color(0xFFF5EEFA)

// ---------- Dark ----------
val VioletInDark = Color(0xFFDDB7FF)
val OnVioletInDark = Color(0xFF4A0080)
val VioletContainerDark = Color(0xFF6A00B0)
val OnVioletContainerDark = Color(0xFFF1DDFF)

val PeacockInDark = Color(0xFF4FD8EB)
val OnPeacockInDark = Color(0xFF00363D)
val PeacockContainerDark = Color(0xFF004F58)

val TerracottaInDark = Color(0xFFFFB59F)
val OnTerracottaInDark = Color(0xFF5F1500)
val TerracottaContainerDark = Color(0xFF862200)

val Aubergine = Color(0xFF120D18)     // fondo oscuro berenjena
val NightText = Color(0xFFEBE0F2)
val NightSurfaceVariant = Color(0xFF4B4256)
val NightTextVariant = Color(0xFFCFC3DA)
val OutlineDark = Color(0xFF988EA3)

// ---------- Degradados listos para usar (cabeceras, banners, botones destacados) ----------
val VioletGradient = Brush.linearGradient(listOf(VioletDark, Violet, Magenta))
val GalleryGradient = Brush.verticalGradient(listOf(Violet, VioletDark, Color(0xFF3A0066)))
val GoldGradient = Brush.linearGradient(listOf(Marigold, MarigoldBright))