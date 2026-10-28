package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IGrupoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class GrupoViewModel(private val grupoRepository: IGrupoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<GrupoUiState>(GrupoUiState.Loading)
    val uiState: StateFlow<GrupoUiState> = _uiState.asStateFlow()
    
    private val _actionResult = MutableStateFlow<Resultado<String>?>(null)
    val actionResult: StateFlow<Resultado<String>?> = _actionResult.asStateFlow()

    fun cargarGrupos(userId: Int) {
        viewModelScope.launch {
            _uiState.value = GrupoUiState.Loading
            grupoRepository.obtenerGrupos(userId)
                .catch { e ->
                    _uiState.value = GrupoUiState.Error(e.message ?: "Error desconocido")
                }
                .collect { resultado ->
                    when (resultado) {
                        is Resultado.Exito -> _uiState.value = GrupoUiState.Success(resultado.data)
                        is Resultado.Error -> _uiState.value = GrupoUiState.Error(resultado.excepcion.message ?: "Error")
                        is Resultado.Cargando -> _uiState.value = GrupoUiState.Loading
                    }
                }
        }
    }
    
    fun crearGrupo(nombre: String, descripcion: String, usuario: Usuario) {
        viewModelScope.launch {
            val nuevoGrupo = Grupo(
                id = (1000..9999).random(), // ID simulado
                nombreGrupo = nombre,
                descripcion = descripcion,
                propietario = usuario,
                miebros = listOf(usuario)
            )
            grupoRepository.crearGrupo(nuevoGrupo)
            _actionResult.value = Resultado.Exito("Grupo creado exitosamente")
        }
    }
    
    fun unirseAGrupo(codigo: String, usuario: Usuario) {
        viewModelScope.launch {
            val resultado = grupoRepository.unirseAGrupo(codigo, usuario)
            when (resultado) {
                is Resultado.Exito -> _actionResult.value = Resultado.Exito("Te has unido a ${resultado.data.nombreGrupo}")
                is Resultado.Error -> _actionResult.value = Resultado.Error(resultado.excepcion)
                is Resultado.Cargando -> {}
            }
        }
    }
    
    fun clearActionResult() {
        _actionResult.value = null
    }

    class Factory(private val grupoRepository: IGrupoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GrupoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GrupoViewModel(grupoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
