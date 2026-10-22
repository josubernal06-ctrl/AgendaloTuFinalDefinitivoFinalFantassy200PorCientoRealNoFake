package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiViewModel(
    private val repository: AiRepository = AiRepository()
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

    fun enviarPromptAGemini() {
        val prompt = _promptText.value
        if (prompt.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _responseText.value = ""
            try {
                val resultado = repository.obtenerRespuestaGemini(prompt)
                _responseText.value = resultado.answer
            } catch (e: Exception) {
                _responseText.value = "Error al conectar con AI: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
