package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MuseoDao {
    @Query("SELECT obraId FROM favoritos WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeFavoriteIds(userId: Long): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE userId = :userId AND obraId = :obraId")
    suspend fun removeFavorite(userId: Long, obraId: Int)

    @Query("SELECT * FROM reservas WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeReservations(userId: Long): Flow<List<ReservaEntity>>

    @Query("SELECT * FROM reservas ORDER BY createdAt DESC")
    fun observeAllReservations(): Flow<List<ReservaEntity>>

    @Insert
    suspend fun addReservation(reservation: ReservaEntity)

    @Update
    suspend fun updateReservation(reservation: ReservaEntity)

    @Query("UPDATE reservas SET estado = 'CANCELADA' WHERE userId = :userId AND id = :reservationId")
    suspend fun cancelReservation(userId: Long, reservationId: Long)

    @Query("DELETE FROM reservas WHERE userId = :userId AND id = :reservationId")
    suspend fun deleteReservation(userId: Long, reservationId: Long)

    @Query("DELETE FROM reservas WHERE userId = :userId AND id = (SELECT id FROM reservas WHERE userId = :userId AND horario = :horario ORDER BY id DESC LIMIT 1)")
    suspend fun deleteLatestReservation(userId: Long, horario: String)

    @Query("SELECT * FROM rutas WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeRoutes(userId: Long): Flow<List<RutaEntity>>

    @Insert
    suspend fun addRoute(route: RutaEntity)

    @Update
    suspend fun updateRoute(route: RutaEntity)

    @Query("UPDATE rutas SET estado = 'ARCHIVADA' WHERE userId = :userId AND id = :routeId")
    suspend fun archiveRoute(userId: Long, routeId: Long)

    @Query("DELETE FROM rutas WHERE userId = :userId AND id = :routeId")
    suspend fun deleteRoute(userId: Long, routeId: Long)

    @Query("SELECT * FROM usuarios WHERE username = :username AND password = :password LIMIT 1")
    suspend fun findUser(username: String, password: String): UserEntity?

    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
    suspend fun findUserByUsername(username: String): UserEntity?

    @Insert
    suspend fun addUser(user: UserEntity): Long
}
