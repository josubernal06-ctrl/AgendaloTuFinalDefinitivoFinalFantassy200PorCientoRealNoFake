package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.logica.GestorHorarios
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IAiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AiViewModel(
    private val repository: IAiRepository = AiRepository()
) : ViewModel() {

    private val _promptText = MutableStateFlow("")
    val promptText: StateFlow<String> = _promptText.asStateFlow()

    private val _responseText = MutableStateFlow("")
    val responseText: StateFlow<String> = _responseText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onPromptChanged(newPrompt: String) {
        _promptText.value = newPrompt
    }

    /**
     * Función agregarActividad() que recibe por parámetro la respuesta de la API de Gemini
     * (o cualquier respuesta procesada), extrae la actividad y la guarda en la base de datos de actividades.
     */
    suspend fun agregarActividad(
        respuestaApi: String,
        context: Context? = null,
        actividadRepository: ActividadRepository? = null,
        usuario: Usuario? = null
    ): AbstractActividad? {
        val actividad = GestorHorarios.parsearActividadDesdeRespuesta(respuestaApi, usuario)
        if (actividad != null) {
            val repoToUse = actividadRepository ?: context?.let {
                val db = AppDatabase.getDatabase(it)
                ActividadRepository(db.actividadDao(), db.alarmDao())
            }
            repoToUse?.let { repo ->
                withContext(Dispatchers.IO) {
                    repo.insertarActividad(actividad)
                }
            }
        }
        return actividad
    }

    /**
     * Concatena el mensaje del usuario con un prompt optimizado y especializado para creación de actividades,
     * realiza el llamado a la API sin alterar el endpoint/invocación de Retrofit,
     * y delega en agregarActividad() el procesamiento de la respuesta para crear la actividad.
     */
    fun enviarPromptAGemini(
        context: Context? = null,
        actividadRepository: ActividadRepository? = null,
        usuario: Usuario? = null,
        onActividadCreada: ((AbstractActividad) -> Unit)? = null
    ) {
        val promptOriginal = _promptText.value
        if (promptOriginal.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _responseText.value = ""
            try {
                // 1. Concatena el mensaje con un prompt optimizado y especializado
                val promptOptimizado = GestorHorarios.construirPromptActividad(promptOriginal)

                // 2. Llamado a la API sin modificar Retrofit ni la firma de obtenerRespuestaGemini
                val resultado = repository.obtenerRespuestaGemini(promptOptimizado)

                // 3. Crear la actividad pasando la respuesta de la API a agregarActividad()
                val nuevaActividad = agregarActividad(
                    respuestaApi = resultado.answer,
                    context = context,
                    actividadRepository = actividadRepository,
                    usuario = usuario
                )

                if (nuevaActividad != null) {
                    _responseText.value = "✅ ¡Actividad agregada a tu horario con éxito!\n\n" +
                            "📌 Título: ${nuevaActividad.titulo}\n" +
                            "📝 Respuesta Gemini: ${resultado.answer}"
                    onActividadCreada?.invoke(nuevaActividad)
                } else {
                    _responseText.value = "Gemini respondió:\n${resultado.answer}"
                }
            } catch (e: Exception) {
                _responseText.value = "Error al conectar con AI: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    class Factory(private val repository: IAiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AiViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AiViewModel(repository) as T
            }
            throw IllegalArgumentException("Clase ViewModel desconocida")
        }
    }
}


