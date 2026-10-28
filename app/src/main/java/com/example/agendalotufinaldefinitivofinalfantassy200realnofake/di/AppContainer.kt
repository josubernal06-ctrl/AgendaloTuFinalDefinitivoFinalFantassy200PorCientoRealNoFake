package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di

import android.content.Context
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.GrupoRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.IActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.IGrupoRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IAiRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IAuthRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock.MockAuthRepository

class AppContainer(context: Context) {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val authRepository: IAuthRepository by lazy {
        MockAuthRepository()
    }

    val actividadRepository: IActividadRepository by lazy {
        ActividadRepository(database.actividadDao(), database.alarmDao())
    }

    val grupoRepository: IGrupoRepository by lazy {
        GrupoRepository(database.grupoDao())
    }

    val aiRepository: IAiRepository by lazy {
        AiRepository()
    }
}
