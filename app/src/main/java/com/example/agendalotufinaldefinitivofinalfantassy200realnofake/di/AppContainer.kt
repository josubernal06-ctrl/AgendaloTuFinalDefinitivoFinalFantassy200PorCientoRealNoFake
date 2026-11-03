package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di

import android.content.Context
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.GrupoRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAuthRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAiRepository

class AppContainer(private val context: Context) {
    // Auth - always mock for now
    val authRepository: IAuthRepository by lazy {
        MockAuthRepository()
    }

    // Grupos - Uses Room + SQLite Cloud (Dev 2)
    val grupoRepository: IGrupoRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        GrupoRepository(database.grupoDao())
    }

    // Actividades - Uses Room by default (Dev 2)
    val actividadRepository: IActividadRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        ActividadRepository(database.actividadDao())
    }

    // AI - Mock for development to avoid unnecessary real API calls
    val aiRepository: IAiRepository by lazy {
        MockAiRepository() // Use AiRepository() for real API
    }

    val themePreferences: com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ThemePreferences by lazy {
        com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ThemePreferences(context)
    }

    val userPreferences: com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.UserPreferences by lazy {
        com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.UserPreferences(context)
    }
}
