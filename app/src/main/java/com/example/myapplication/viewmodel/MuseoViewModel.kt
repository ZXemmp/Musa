package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.MuseoDatabase
import com.example.myapplication.data.repository.MuseoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MuseoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MuseoRepository(MuseoDatabase.getInstance(application).museoDao())
    val favoriteIds: StateFlow<Set<Int>> = repository.favoriteIds
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())
    val reservations = repository.reservations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun toggleFavorite(obraId: Int) = viewModelScope.launch {
        repository.toggleFavorite(obraId, obraId in favoriteIds.value)
    }

    fun addReservation(obraId: Int, horario: String, visitantes: Int) = viewModelScope.launch {
        if (horario.isNotBlank() && visitantes > 0) repository.addReservation(obraId, horario, visitantes)
    }

    fun cancelReservation(id: Long) = viewModelScope.launch { repository.cancelReservation(id) }
    fun deleteReservation(id: Long) = viewModelScope.launch { repository.deleteReservation(id) }
    fun deleteLatestReservation(horario: String) = viewModelScope.launch {
        repository.deleteLatestReservation(horario)
    }
}
