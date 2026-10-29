package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import kotlinx.coroutines.flow.Flow

interface IActividadRepository {
    fun obtenerActividades(): Flow<Resultado<List<AbstractActividad>>>
    suspend fun insertarActividad(actividad: AbstractActividad)
    suspend fun eliminarActividad(actividad: AbstractActividad)
}
