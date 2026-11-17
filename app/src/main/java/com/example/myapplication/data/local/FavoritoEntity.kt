package com.example.myapplication.data.local

import androidx.room.Entity

@Entity(tableName = "favoritos", primaryKeys = ["userId", "obraId"])
data class FavoritoEntity(
    val userId: Long,
    val obraId: Int,
    val createdAt: Long = System.currentTimeMillis()
)
