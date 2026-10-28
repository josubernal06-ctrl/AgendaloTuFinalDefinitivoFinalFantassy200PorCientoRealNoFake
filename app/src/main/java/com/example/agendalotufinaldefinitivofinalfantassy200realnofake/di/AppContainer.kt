package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di

import android.content.Context
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAuthRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockGrupoRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAiRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiRepository

class AppContainer(private val context: Context) {
    // Auth - always mock for now
    val authRepository: IAuthRepository by lazy {
        MockAuthRepository()
    }

    // Grupos - always mock for now
    val grupoRepository: IGrupoRepository by lazy {
        MockGrupoRepository()
    }

    // Actividades - Uses Room by default (Dev 2), but could be swapped
    val actividadRepository: IActividadRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        ActividadRepository(database.actividadDao())
    }

    // AI - Mock for development to avoid unnecessary real API calls
    val aiRepository: IAiRepository by lazy {
        MockAiRepository() // Use AiRepository() for real API
    }
}
