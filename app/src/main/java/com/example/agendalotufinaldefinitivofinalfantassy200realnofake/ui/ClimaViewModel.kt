package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.clima.ClimaRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Clima
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClimaViewModel : ViewModel() {

    private val repository = ClimaRepository()

    private val _climaState = MutableStateFlow<Clima?>(null)
    val climaState: StateFlow<Clima?> = _climaState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchClima()
    }

    fun fetchClima() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                val clima = repository.obtenerClimaActual()
                _climaState.value = clima
            } catch (e: Exception) {
                _errorState.value = "Error al obtener clima: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
