package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di

import android.content.Context
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.GrupoRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.IActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.IGrupoRepository

class AppContainer(context: Context) {
    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val actividadRepository: IActividadRepository by lazy {
        ActividadRepository(database.actividadDao())
    }

    val grupoRepository: IGrupoRepository by lazy {
        GrupoRepository(database.grupoDao())
    }
}
