package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class ReservaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val obraId: Int,
    val horario: String,
    val visitantes: Int,
    val estado: String = "CONFIRMADA",
    val createdAt: Long = System.currentTimeMillis()
)
