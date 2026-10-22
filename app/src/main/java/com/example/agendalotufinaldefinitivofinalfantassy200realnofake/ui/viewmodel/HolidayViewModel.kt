package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.HolidayRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.model.Holiday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface HolidayUiState {
    data object Loading : HolidayUiState
    data class Success(val holidays: List<Holiday>) : HolidayUiState
    data class Error(val message: String) : HolidayUiState
}

class HolidayViewModel(
    private val repository: HolidayRepository = HolidayRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HolidayUiState>(HolidayUiState.Loading)
    val uiState: StateFlow<HolidayUiState> = _uiState.asStateFlow()

    init {
        cargarFeriadosBolivia()
    }

    fun cargarFeriadosBolivia(countryCode: String = "BO", year: Int = 2026) {
        viewModelScope.launch {
            _uiState.value = HolidayUiState.Loading
            repository.getHolidays(countryCode, year)
                .onSuccess { list ->
                    _uiState.value = HolidayUiState.Success(list)
                }
                .onFailure { error ->
                    _uiState.value = HolidayUiState.Error(error.message ?: "Error al cargar feriados")
                }
        }
    }
}
