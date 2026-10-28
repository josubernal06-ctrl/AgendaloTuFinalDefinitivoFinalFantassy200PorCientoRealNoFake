package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.actividad.ActividadDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ActividadRepository(
    private val dao: ActividadDao,
    private val alarmDao: AlarmDao? = null
) {

    fun obtenerActividades(): Flow<Resultado<List<AbstractActividad>>> {
        return dao.obtenerTodasLasActividades().map { entities ->
            Resultado.Exito(entities.map { it.toDomain() }) as Resultado<List<AbstractActividad>>
        }.catch { e ->
            emit(Resultado.Error(e))
        }
    }

    suspend fun insertarActividad(actividad: AbstractActividad) {
        withContext(Dispatchers.IO) {
            val entity = actividad.toEntity()
            val rowId = dao.insertarActividad(entity)
            val realId = if (rowId > 0) rowId.toInt() else entity.id

            // Programar automáticamente la alarma 40 minutos antes del inicio de la actividad
            val inicioDateTime = try {
                LocalDateTime.parse(entity.inicio)
            } catch (e: Exception) {
                LocalDateTime.now().plusHours(1)
            }

            val fechaHoraAlarma = inicioDateTime.minusMinutes(40)

            val alarmaEntity = AlarmEntity(
                actividadId = realId,
                tituloActividad = entity.titulo,
                fechaHoraAlarma = fechaHoraAlarma.toString(),
                estaActiva = true
            )

            alarmDao?.insertarAlarma(alarmaEntity)
        }
    }

    suspend fun eliminarActividad(actividad: AbstractActividad) {
        withContext(Dispatchers.IO) {
            val entity = actividad.toEntity()
            dao.eliminarActividad(entity)
            alarmDao?.eliminarAlarmaPorActividadId(entity.id)
        }
    }
}

