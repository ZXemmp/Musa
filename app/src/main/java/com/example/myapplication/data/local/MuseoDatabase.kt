package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoritoEntity::class, ReservaEntity::class], version = 1, exportSchema = false)
abstract class MuseoDatabase : RoomDatabase() {
    abstract fun museoDao(): MuseoDao

    companion object {
        @Volatile private var INSTANCE: MuseoDatabase? = null

        fun getInstance(context: Context): MuseoDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MuseoDatabase::class.java,
                "musa.db"
            ).build().also { INSTANCE = it }
        }
    }
}
