package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository


import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import kotlinx.coroutines.flow.Flow

/**
 * Contrato oficial para el manejo de actividades en la capa de datos.
 * Define las operaciones que cualquier repositorio de actividades (Room, SQLite Cloud, etc.)
 * debe ofrecer a los ViewModels.
 */
interface IActividadRepository {
    fun obtenerActividades(): Flow<Resultado<List<AbstractActividad>>>
    suspend fun insertarActividad(actividad: AbstractActividad)
    suspend fun eliminarActividad(actividad: AbstractActividad)
}