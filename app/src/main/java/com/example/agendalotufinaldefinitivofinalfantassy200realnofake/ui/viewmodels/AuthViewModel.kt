package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: IAuthRepository) : ViewModel() {

    val currentUser: StateFlow<Usuario?> = authRepository.currentUser

    private val _testUsers = MutableStateFlow<List<Usuario>>(emptyList())
    val testUsers: StateFlow<List<Usuario>> = _testUsers.asStateFlow()

    init {
        viewModelScope.launch {
            _testUsers.value = authRepository.getTestUsers()
        }
    }

    fun login(email: String) {
        viewModelScope.launch {
            authRepository.login(email)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    class Factory(private val authRepository: IAuthRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
