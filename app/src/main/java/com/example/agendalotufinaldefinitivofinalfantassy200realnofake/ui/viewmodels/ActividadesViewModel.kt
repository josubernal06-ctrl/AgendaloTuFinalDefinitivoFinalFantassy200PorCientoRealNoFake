package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IActividadRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * VIEWMODEL DE GESTIÓN DE ACTIVIDADES (ARQUITECTURA MVVM).
 * Expone el estado inmutable ActividadUiState para las pantallas Compose.
 * Cancela corrutinas activas al cambiar de sesión para evitar fugas de datos.
 */
class ActividadesViewModel(private val repository: IActividadRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ActividadUiState())
    val uiState: StateFlow<ActividadUiState> = _uiState.asStateFlow()

    private var cargarJob: Job? = null

    /** Inicia la recolección del flujo de actividades cancelando cualquier Job previo. */
    fun cargarActividades(propietarioId: Int? = null) {
        cargarJob?.cancel()
        cargarJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, actividades = emptyList()) }
            repository.obtenerActividades(propietarioId).collect { resultado ->
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

    /** Resetea el estado y cancela las corrutinas al cerrar sesión o cambiar de usuario. */
    fun limpiar() {
        cargarJob?.cancel()
        _uiState.value = ActividadUiState()
    }

    /** Crea una nueva actividad y la persiste local y remotamente. */
    fun crearActividad(actividad: AbstractActividad) {
        viewModelScope.launch {
            repository.insertarActividad(actividad)
        }
    }

    /** Elimina una actividad. */
    fun eliminarActividad(actividad: AbstractActividad) {
        viewModelScope.launch {
            repository.eliminarActividad(actividad)
        }
    }

    /** Factory para instanciar ActividadesViewModel con inyección de IActividadRepository. */
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
