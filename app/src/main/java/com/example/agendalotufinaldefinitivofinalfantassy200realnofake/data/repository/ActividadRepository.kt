package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import android.content.Context
import android.util.Log
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.alarm.AlarmScheduler
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.SqliteCloudDataSource
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IActividadRepository
import com.google.firebase.auth.FirebaseAuth
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
    private val context: Context? = null,
    private val remoteDataSource: SqliteCloudDataSource = SqliteCloudDataSource()
) : IActividadRepository {

    override fun obtenerActividades(): Flow<Resultado<List<AbstractActividad>>> {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        return dao.obtenerActividadesPorUsuario(currentUserId).map { entities ->
            Resultado.Exito(entities.map { it.toDomain() }) as Resultado<List<AbstractActividad>>
        }.catch { e ->
            emit(Resultado.Error(e))
        }
    }

    override suspend fun insertarActividad(actividad: AbstractActividad) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "local_user"
        val entity = actividad.toEntity(currentUserId)

        // 1. Guardar primero en Room local (inmediato) + Alarma automática y programar en AlarmManager
        withContext(Dispatchers.IO) {
            val rowId = dao.insertarActividad(entity)
            val realId = if (rowId > 0L) rowId.toInt() else entity.id

            // Programar automáticamente la alarma 30 minutos antes del inicio de la actividad
            val inicioDateTime = try {
                LocalDateTime.parse(entity.fechaInicio)
            } catch (e: Exception) {
                LocalDateTime.now().plusHours(1)
            }

            val fechaHoraAlarma = inicioDateTime.minusMinutes(30)

            val alarmaEntity = AlarmEntity(
                actividadId = realId,
                tituloActividad = entity.titulo,
                fechaHoraAlarma = fechaHoraAlarma.toString(),
                estaActiva = true
            )

            alarmDao?.insertarAlarma(alarmaEntity)

            if (context != null) {
                try {
                    val scheduler = AlarmScheduler(context)
                    scheduler.programarAlarma(realId, entity.titulo, fechaHoraAlarma)
                    Log.d("ActividadRepository", "Alarma programada correctamente para: ${entity.titulo} a las $fechaHoraAlarma")
                } catch (e: Exception) {
                    Log.e("ActividadRepository", "Error al programar alarma con AlarmManager: ${e.message}", e)
                }
            }
        }

        // 2. Enviar a SQLite Cloud (tabla ACTIVITY) sin ser cancelado al cambiar de pantalla
        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val tituloClean = entity.titulo.replace("'", "''")
                val descClean = entity.descripcion.replace("'", "''")
                val userIdClean = entity.userId.replace("'", "''")
                val grupoIdVal = entity.grupoId ?: "NULL"
                val esPrivadaVal = if (entity.esPrivada) 1 else 0
                val esConjuntaVal = if (entity.esConjunta) 1 else 0

                val sqlInsert = """
                    INSERT INTO ACTIVITY (id, titulo, descripcion, userId, grupoId, esConjunta, esPrivada, fechaInicio, fechaFin)
                    VALUES (${entity.id}, '$tituloClean', '$descClean', '$userIdClean', $grupoIdVal, $esConjuntaVal, $esPrivadaVal, '${entity.fechaInicio}', '${entity.fechaFin}');
                """.trimIndent()

                remoteDataSource.ejecutarConsulta(apiKey, sqlInsert)
            }
        }
    }

    override suspend fun eliminarActividad(actividad: AbstractActividad) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "local_user"
        val entity = actividad.toEntity(currentUserId)

        withContext(Dispatchers.IO) {
            dao.eliminarActividad(entity)
            alarmDao?.eliminarAlarmaPorActividadId(entity.id)
            if (context != null) {
                try {
                    val scheduler = AlarmScheduler(context)
                    scheduler.cancelarAlarma(entity.id)
                } catch (e: Exception) {
                    Log.e("ActividadRepository", "Error al cancelar alarma: ${e.message}")
                }
            }
        }

        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val sqlDelete = "DELETE FROM ACTIVITY WHERE id = ${entity.id};"
                remoteDataSource.ejecutarConsulta(apiKey, sqlDelete)
            }
        }
    }
}
