package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MuseoDao {
    @Query("SELECT obraId FROM favoritos ORDER BY createdAt DESC")
    fun observeFavoriteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoritoEntity)

    @Query("DELETE FROM favoritos WHERE obraId = :obraId")
    suspend fun removeFavorite(obraId: Int)

    @Query("SELECT * FROM reservas ORDER BY createdAt DESC")
    fun observeReservations(): Flow<List<ReservaEntity>>

    @Insert
    suspend fun addReservation(reservation: ReservaEntity)

    @Query("UPDATE reservas SET estado = 'CANCELADA' WHERE id = :reservationId")
    suspend fun cancelReservation(reservationId: Long)

    @Query("DELETE FROM reservas WHERE id = :reservationId")
    suspend fun deleteReservation(reservationId: Long)

    @Query("DELETE FROM reservas WHERE id = (SELECT id FROM reservas WHERE horario = :horario ORDER BY id DESC LIMIT 1)")
    suspend fun deleteLatestReservation(horario: String)
}
