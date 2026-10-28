package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import kotlinx.coroutines.flow.StateFlow

interface IAuthRepository {
    val currentUser: StateFlow<Usuario?>
    
    suspend fun getTestUsers(): List<Usuario>
    suspend fun login(email: String): Boolean
    suspend fun logout()
}
