package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.IActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActividadesViewModel(private val repository: IActividadRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ActividadUiState())
    val uiState: StateFlow<ActividadUiState> = _uiState.asStateFlow()

    init {
        cargarActividades()
    }

    private fun cargarActividades() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.obtenerActividades().collect { resultado ->
                when (resultado) {
                    is Resultado.Exito -> {
                        _uiState.update { it.copy(isLoading = false, actividades = resultado.data, error = null) }
                    }
                    is Resultado.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = resultado.excepcion.message) }
                    }
                    Resultado.Cargando -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun crearActividad(actividad: AbstractActividad) {
        viewModelScope.launch {
            repository.insertarActividad(actividad)
        }
    }

    fun eliminarActividad(actividad: AbstractActividad) {
        viewModelScope.launch {
            repository.eliminarActividad(actividad)
        }
    }

    class Factory(private val repository: IActividadRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ActividadesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ActividadesViewModel(repository) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}
