package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IGrupoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * VIEWMODEL DE GESTIÓN DE GRUPOS (ARQUITECTURA MVVM).
 * Gestiona el estado de grupos y membresías para las pantallas de la app.
 * Controla corrutinas activas con `initJob` para evitar fugas de información entre cuentas.
 */
class GrupoViewModel(private val grupoRepository: IGrupoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<GrupoUiState>(GrupoUiState.Loading)
    val uiState: StateFlow<GrupoUiState> = _uiState.asStateFlow()

    private val _actionResult = MutableStateFlow<Resultado<String>?>(null)
    val actionResult: StateFlow<Resultado<String>?> = _actionResult.asStateFlow()

    private var initJob: Job? = null

    /** Dispara la sincronización en segundo plano de grupos desde SQLite Cloud. */
    fun sincronizarGrupos(userId: Int) {
        viewModelScope.launch {
            grupoRepository.sincronizarGrupos(userId)
        }
    }

    /** Inicia la escucha continua de grupos en Room para el usuario actual cancelando corrutinas previas. */
    fun init(userId: Int) {
        initJob?.cancel()
        initJob = viewModelScope.launch {
            _uiState.value = GrupoUiState.Loading
            grupoRepository.sincronizarGrupos(userId)
            
            grupoRepository.obtenerGrupos(userId)
                .collect { resultado ->
                    when (resultado) {
                        is Resultado.Exito -> _uiState.value = GrupoUiState.Success(resultado.data)
                        is Resultado.Error -> _uiState.value = GrupoUiState.Error(resultado.excepcion.message ?: "Error al cargar grupos")
                        is Resultado.Cargando -> _uiState.value = GrupoUiState.Loading
                    }
                }
        }
    }

    /** Limpia el estado interno y cancela observadores activados al cerrar sesión. */
    fun limpiar() {
        initJob?.cancel()
        _uiState.value = GrupoUiState.Loading
        _actionResult.value = null
    }

    /** Registra un nuevo grupo con código de invitación único. */
    fun crearGrupo(nombre: String, descripcion: String, usuario: Usuario) {
        viewModelScope.launch {
            val randomCode = "GRP" + (100000..999999).random()
            val nuevoGrupo = Grupo(
                id = 0,
                nombreGrupo = nombre,
                descripcion = descripcion,
                propietario = usuario,
                codigoInvitacion = randomCode,
                miebros = listOf(usuario)
            )
            grupoRepository.crearGrupo(nuevoGrupo)
            _actionResult.value = Resultado.Exito("Grupo creado exitosamente")
        }
    }

    /** Elimina un grupo si el usuario es propietario. */
    fun eliminarGrupo(grupo: Grupo, userId: Int) {
        viewModelScope.launch {
            try {
                grupoRepository.eliminarGrupo(grupo)
                _actionResult.value = Resultado.Exito("Grupo eliminado exitosamente")
                sincronizarGrupos(userId)
            } catch (e: Exception) {
                _actionResult.value = Resultado.Error(e)
            }
        }
    }

    /** Elimina únicamente la membresía del usuario actual del grupo. */
    fun salirDelGrupo(grupo: Grupo, userId: Int) {
        viewModelScope.launch {
            try {
                grupoRepository.salirDelGrupo(grupo)
                _actionResult.value = Resultado.Exito("Has salido del grupo")
                sincronizarGrupos(userId)
            } catch (e: Exception) {
                _actionResult.value = Resultado.Error(e)
            }
        }
    }

    /** Valida el código de invitación e integra al usuario en el grupo. */
    fun unirseAGrupo(codigo: String, usuario: Usuario) {
        viewModelScope.launch {
            _actionResult.value = Resultado.Cargando
            val resultado = grupoRepository.unirseAGrupo(codigo, usuario)
            when (resultado) {
                is Resultado.Exito -> {
                    _actionResult.value = Resultado.Exito("Te has unido al grupo ${resultado.data.nombreGrupo}")
                    sincronizarGrupos(usuario.id)
                }
                is Resultado.Error -> _actionResult.value = Resultado.Error(resultado.excepcion)
                Resultado.Cargando -> {}
            }
        }
    }

    /** Limpia los mensajes de resultado de operaciones (para Snacks/Dialogs). */
    fun clearActionResult() {
        _actionResult.value = null
    }

    /** Factory para instanciar GrupoViewModel con inyección de IGrupoRepository. */
    class Factory(private val grupoRepository: IGrupoRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GrupoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GrupoViewModel(grupoRepository) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}
