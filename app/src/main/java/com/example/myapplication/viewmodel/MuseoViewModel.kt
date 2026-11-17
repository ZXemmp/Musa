package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.MuseoDatabase
import com.example.myapplication.data.local.UserEntity
import com.example.myapplication.data.repository.MuseoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MuseoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MuseoRepository(MuseoDatabase.getInstance(application).museoDao())
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser
    val favoriteIds: StateFlow<Set<Int>> = _currentUser.flatMapLatest { user ->
        user?.let { repository.favoriteIds(it.id).map { ids -> ids.toSet() } } ?: flowOf(emptySet())
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())
    val reservations = _currentUser.flatMapLatest { user ->
        user?.let { repository.reservations(it.id) } ?: flowOf(emptyList())
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val allReservations = repository.allReservations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val routes = _currentUser.flatMapLatest { user ->
        user?.let { repository.routes(it.id) } ?: flowOf(emptyList())
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun login(username: String, password: String, onResult: (Boolean) -> Unit) = viewModelScope.launch {
        val user = repository.login(username.trim(), password)
        _currentUser.value = user
        onResult(user != null)
    }

    fun createUser(name: String, username: String, password: String, onResult: (Boolean) -> Unit) = viewModelScope.launch {
        if (name.isBlank() || username.isBlank() || password.isBlank() || repository.usernameExists(username.trim())) {
            onResult(false)
            return@launch
        }
        val id = repository.createUser(UserEntity(nombre = name.trim(), username = username.trim(), password = password))
        _currentUser.value = UserEntity(id = id, nombre = name.trim(), username = username.trim(), password = password)
        onResult(true)
    }

    fun continueAsGuest() = viewModelScope.launch {
        val username = "invitado_${System.currentTimeMillis()}"
        val id = repository.createUser(UserEntity(nombre = "Invitado", username = username, password = "", invitado = true))
        _currentUser.value = UserEntity(id = id, nombre = "Invitado", username = username, password = "", invitado = true)
    }

    fun logout() { _currentUser.value = null }

    fun toggleFavorite(obraId: Int) = viewModelScope.launch {
        _currentUser.value?.let { repository.toggleFavorite(it.id, obraId, obraId in favoriteIds.value) }
    }

    fun addReservation(obraId: Int, horario: String, visitantes: Int) = viewModelScope.launch {
        _currentUser.value?.let { if (horario.isNotBlank() && visitantes > 0) repository.addReservation(it.id, obraId, horario, visitantes) }
    }

    fun updateReservation(id: Long, horario: String, visitantes: Int) = viewModelScope.launch {
        _currentUser.value?.let { if (horario.isNotBlank() && visitantes > 0) repository.updateReservation(it.id, id, horario, visitantes) }
    }

    fun cancelReservation(id: Long) = viewModelScope.launch { _currentUser.value?.let { repository.cancelReservation(it.id, id) } }
    fun deleteReservation(id: Long) = viewModelScope.launch { _currentUser.value?.let { repository.deleteReservation(it.id, id) } }
    fun deleteLatestReservation(horario: String) = viewModelScope.launch {
        _currentUser.value?.let { repository.deleteLatestReservation(it.id, horario) }
    }

    fun addRoute(name: String, mode: String, workIds: String) = viewModelScope.launch {
        _currentUser.value?.let { if (name.isNotBlank() && workIds.isNotBlank()) repository.addRoute(it.id, name, mode, workIds) }
    }
    fun updateRoute(id: Long, name: String, mode: String, workIds: String) = viewModelScope.launch {
        _currentUser.value?.let { if (name.isNotBlank() && workIds.isNotBlank()) repository.updateRoute(it.id, id, name, mode, workIds) }
    }
    fun archiveRoute(id: Long) = viewModelScope.launch { _currentUser.value?.let { repository.archiveRoute(it.id, id) } }
    fun deleteRoute(id: Long) = viewModelScope.launch { _currentUser.value?.let { repository.deleteRoute(it.id, id) } }
}
