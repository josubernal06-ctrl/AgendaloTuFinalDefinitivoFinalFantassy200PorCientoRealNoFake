package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad

data class ActividadUiState(
    val isLoading: Boolean = true,
    val actividades: List<AbstractActividad> = emptyList(),
    val error: String? = null
)
