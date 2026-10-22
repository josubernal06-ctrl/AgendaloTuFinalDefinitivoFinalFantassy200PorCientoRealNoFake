package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ActividadRepository(private val dao: ActividadDao) {

    fun obtenerActividades(): Flow<Resultado<List<AbstractActividad>>> {
        return dao.obtenerTodasLasActividades().map { entities ->
            Resultado.Exito(entities.map { it.toDomain() }) as Resultado<List<AbstractActividad>>
        }.catch { e ->
            emit(Resultado.Error(e))
        }
    }

    suspend fun insertarActividad(actividad: AbstractActividad) {
        withContext(Dispatchers.IO) {
            dao.insertarActividad(actividad.toEntity())
        }
    }

    suspend fun eliminarActividad(actividad: AbstractActividad) {
        withContext(Dispatchers.IO) {
            dao.eliminarActividad(actividad.toEntity())
        }
    }
}
