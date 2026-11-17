package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rutas")
data class RutaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val nombre: String,
    val modo: String,
    val obraIds: String,
    val estado: String = "ACTIVA",
    val createdAt: Long = System.currentTimeMillis()
)
