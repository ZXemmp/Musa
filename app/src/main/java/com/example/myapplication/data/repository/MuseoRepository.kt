package com.example.myapplication.data.repository

import com.example.myapplication.data.local.FavoritoEntity
import com.example.myapplication.data.local.MuseoDao
import com.example.myapplication.data.local.ReservaEntity
import com.example.myapplication.data.local.RutaEntity
import com.example.myapplication.data.local.UserEntity

class MuseoRepository(private val dao: MuseoDao) {
    fun favoriteIds(userId: Long) = dao.observeFavoriteIds(userId)
    fun reservations(userId: Long) = dao.observeReservations(userId)
    fun allReservations() = dao.observeAllReservations()
    fun routes(userId: Long) = dao.observeRoutes(userId)

    suspend fun toggleFavorite(userId: Long, obraId: Int, isFavorite: Boolean) {
        if (isFavorite) dao.removeFavorite(userId, obraId) else dao.addFavorite(FavoritoEntity(userId, obraId))
    }

    suspend fun addReservation(userId: Long, obraId: Int, horario: String, visitantes: Int) =
        dao.addReservation(ReservaEntity(userId = userId, obraId = obraId, horario = horario, visitantes = visitantes))

    suspend fun updateReservation(userId: Long, id: Long, horario: String, visitantes: Int) =
        dao.updateReservation(ReservaEntity(id = id, userId = userId, obraId = 0, horario = horario, visitantes = visitantes))

    suspend fun cancelReservation(userId: Long, id: Long) = dao.cancelReservation(userId, id)
    suspend fun deleteReservation(userId: Long, id: Long) = dao.deleteReservation(userId, id)
    suspend fun deleteLatestReservation(userId: Long, horario: String) = dao.deleteLatestReservation(userId, horario)

    suspend fun addRoute(userId: Long, name: String, mode: String, workIds: String) =
        dao.addRoute(RutaEntity(userId = userId, nombre = name, modo = mode, obraIds = workIds))
    suspend fun updateRoute(userId: Long, id: Long, name: String, mode: String, workIds: String) =
        dao.updateRoute(RutaEntity(id = id, userId = userId, nombre = name, modo = mode, obraIds = workIds))
    suspend fun archiveRoute(userId: Long, id: Long) = dao.archiveRoute(userId, id)
    suspend fun deleteRoute(userId: Long, id: Long) = dao.deleteRoute(userId, id)

    suspend fun login(username: String, password: String) = dao.findUser(username, password)
    suspend fun usernameExists(username: String) = dao.findUserByUsername(username) != null
    suspend fun createUser(user: UserEntity) = dao.addUser(user)
}
