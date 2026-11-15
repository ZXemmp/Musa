package com.example.myapplication.model

import androidx.annotation.DrawableRes

data class Obra(
    @DrawableRes val imagen: Int,
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
