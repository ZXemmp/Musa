package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [FavoritoEntity::class, ReservaEntity::class, RutaEntity::class, UserEntity::class], version = 3, exportSchema = false)
abstract class MuseoDatabase : RoomDatabase() {
    abstract fun museoDao(): MuseoDao

    companion object {
        @Volatile private var INSTANCE: MuseoDatabase? = null

        fun getInstance(context: Context): MuseoDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MuseoDatabase::class.java,
                "musa.db"
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().also { INSTANCE = it }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS rutas (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, modo TEXT NOT NULL, obraIds TEXT NOT NULL, estado TEXT NOT NULL, createdAt INTEGER NOT NULL)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nombre TEXT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL, invitado INTEGER NOT NULL, createdAt INTEGER NOT NULL)")
                database.execSQL("INSERT OR IGNORE INTO usuarios(id, nombre, username, password, invitado, createdAt) VALUES (1, 'Usuario', 'usuario', 'usuario', 0, strftime('%s','now') * 1000)")
                database.execSQL("CREATE TABLE favoritos_new (userId INTEGER NOT NULL, obraId INTEGER NOT NULL, createdAt INTEGER NOT NULL, PRIMARY KEY(userId, obraId))")
                database.execSQL("INSERT INTO favoritos_new(userId, obraId, createdAt) SELECT 1, obraId, createdAt FROM favoritos")
                database.execSQL("DROP TABLE favoritos")
                database.execSQL("ALTER TABLE favoritos_new RENAME TO favoritos")
                database.execSQL("ALTER TABLE reservas ADD COLUMN userId INTEGER NOT NULL DEFAULT 1")
                database.execSQL("ALTER TABLE rutas ADD COLUMN userId INTEGER NOT NULL DEFAULT 1")
            }
        }
    }
}
