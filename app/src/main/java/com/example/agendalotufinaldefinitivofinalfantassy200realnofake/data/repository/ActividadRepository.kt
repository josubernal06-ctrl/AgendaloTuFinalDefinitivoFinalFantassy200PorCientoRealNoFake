package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.actividad.ActividadDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.SqliteCloudDataSource
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.IActividadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ActividadRepository(
    private val dao: ActividadDao,
    private val alarmDao: AlarmDao? = null,
    private val remoteDataSource: SqliteCloudDataSource = SqliteCloudDataSource()
) : IActividadRepository {

    override fun obtenerActividades(): Flow<Resultado<List<AbstractActividad>>> {
        return dao.obtenerTodasLasActividades().map { entities ->
            Resultado.Exito(entities.map { it.toDomain() }) as Resultado<List<AbstractActividad>>
        }.catch { e ->
            emit(Resultado.Error(e))
        }
    }

    override suspend fun insertarActividad(actividad: AbstractActividad) {
        val entity = actividad.toEntity()

        // 1. Guardar primero en Room local (inmediato) + Alarma automática
        withContext(Dispatchers.IO) {
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

        // 2. Enviar a SQLite Cloud sin ser cancelado al cambiar de pantalla
        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val tituloClean = entity.titulo.replace("'", "''")
                val descClean = entity.descripcion.replace("'", "''")
                val ownerClean = entity.propietarioNickname.replace("'", "''")
                val esPrivadaVal = if (entity.esPrivada) 1 else 0
                val esConjuntaVal = if (entity.esConjunta) 1 else 0

                val sqlInsert = """
                    INSERT INTO actividades (titulo, descripcion, esPrivada, propietarioId, propietarioNickname, inicio, fin, esConjunta, puntuacion)
                    VALUES ('$tituloClean', '$descClean', $esPrivadaVal, ${entity.propietarioId}, '$ownerClean', '${entity.inicio}', '${entity.fin}', $esConjuntaVal, ${entity.puntuacion});
                """.trimIndent()

                remoteDataSource.ejecutarConsulta(apiKey, sqlInsert)
            }
        }
    }

    override suspend fun eliminarActividad(actividad: AbstractActividad) {
        val entity = actividad.toEntity()

        withContext(Dispatchers.IO) {
            dao.eliminarActividad(entity)
            alarmDao?.eliminarAlarmaPorActividadId(entity.id)
        }

        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val tituloClean = entity.titulo.replace("'", "''")
                val sqlDelete = "DELETE FROM actividades WHERE titulo = '$tituloClean';"
                remoteDataSource.ejecutarConsulta(apiKey, sqlDelete)
            }
        }
    }
}
