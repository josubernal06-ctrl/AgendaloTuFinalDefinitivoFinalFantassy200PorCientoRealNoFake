package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IAuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAuthRepository : IAuthRepository {
    private val _currentUser = MutableStateFlow<Usuario?>(null)
    override val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    private val testUsers = listOf(
        Usuario(id = 1, nickname = "dev1_user", email = "dev1@agendalotu.com"),
        Usuario(id = 2, nickname = "test_user2", email = "test2@agendalotu.com"),
        Usuario(id = 3, nickname = "demo_user", email = "demo@agendalotu.com")
    )

    override suspend fun getTestUsers(): List<Usuario> {
        delay(300) // Simulating network or DB delay
        return testUsers
    }

    override suspend fun login(email: String): Boolean {
        delay(500) // Simulate network call
        val user = testUsers.find { it.email == email }
        if (user != null) {
            _currentUser.value = user
            return true
        }
        return false
    }

    override suspend fun logout() {
        delay(300) // Simulate network call
        _currentUser.value = null
    }
}
