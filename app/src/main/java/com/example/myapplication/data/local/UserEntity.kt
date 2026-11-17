package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val username: String,
    val password: String,
    val invitado: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
