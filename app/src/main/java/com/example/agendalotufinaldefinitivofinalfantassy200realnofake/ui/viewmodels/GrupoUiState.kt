package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo

sealed interface GrupoUiState {
    data object Loading : GrupoUiState
    data class Success(val grupos: List<Grupo>) : GrupoUiState
    data class Error(val message: String) : GrupoUiState
}
