package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di

import android.content.Context
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAiRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAuthRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockGrupoRepository

class AppContainer(private val context: Context) {
    // Auth - mock repository for session
    val authRepository: IAuthRepository by lazy {
        MockAuthRepository()
    }

    // Grupos - mock repository
    val grupoRepository: IGrupoRepository by lazy {
        MockGrupoRepository()
    }

    // Actividades - Uses Room with AlarmDao integration
    val actividadRepository: IActividadRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        ActividadRepository(database.actividadDao(), database.alarmDao())
    }

    // AI - Real Gemini API Repository
    val aiRepository: IAiRepository by lazy {
        AiRepository()
    }
}

