package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di

import android.content.Context
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.GrupoRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAuthRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAiRepository

/**
 * CONTENEDOR DE INYECCIÓN DE DEPENDENCIAS (MANUAL DI)
 * 
 * Centraliza la creación perezosa (by lazy) de repositorios y bases de datos locales
 * garantizando una única instancia compartida (Singleton) en toda la aplicación.
 */
class AppContainer(private val context: Context) {

    // Repositorio de Autenticación
    val authRepository: IAuthRepository by lazy {
        MockAuthRepository()
    }

    // Repositorio de Grupos (Room + SQLite Cloud)
    val grupoRepository: IGrupoRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        GrupoRepository(database.grupoDao())
    }

    // Repositorio de Actividades (Room + SQLite Cloud + AlarmManager)
    val actividadRepository: IActividadRepository by lazy {
        val database = AppDatabase.getDatabase(context)
        ActividadRepository(database.actividadDao(), database.alarmDao(), context)
    }

    // Repositorio de Inteligencia Artificial (Gemini Prompt API)
    val aiRepository: IAiRepository by lazy {
        MockAiRepository()
    }

    // Preferencias de Temas Visuales (SharedPreferences)
    val themePreferences: com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ThemePreferences by lazy {
        com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ThemePreferences(context)
    }

    // Preferencias de Usuario y Nicknames (SharedPreferences)
    val userPreferences: com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.UserPreferences by lazy {
        com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.UserPreferences(context)
    }
}
