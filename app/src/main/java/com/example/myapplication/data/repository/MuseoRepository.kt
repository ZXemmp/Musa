package com.example.myapplication.data.repository

import com.example.myapplication.data.local.FavoritoEntity
import com.example.myapplication.data.local.MuseoDao
import com.example.myapplication.data.local.ReservaEntity

class MuseoRepository(private val dao: MuseoDao) {
    val favoriteIds = dao.observeFavoriteIds()
    val reservations = dao.observeReservations()

    suspend fun toggleFavorite(obraId: Int, isFavorite: Boolean) {
        if (isFavorite) dao.removeFavorite(obraId) else dao.addFavorite(FavoritoEntity(obraId))
    }

    suspend fun addReservation(obraId: Int, horario: String, visitantes: Int) =
        dao.addReservation(ReservaEntity(obraId = obraId, horario = horario, visitantes = visitantes))

    suspend fun cancelReservation(id: Long) = dao.cancelReservation(id)
    suspend fun deleteReservation(id: Long) = dao.deleteReservation(id)
    suspend fun deleteLatestReservation(horario: String) = dao.deleteLatestReservation(horario)
}
