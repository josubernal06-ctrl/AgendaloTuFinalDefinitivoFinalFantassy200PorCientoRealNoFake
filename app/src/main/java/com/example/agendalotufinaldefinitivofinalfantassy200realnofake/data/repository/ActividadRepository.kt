package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadDao
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

class ActividadRepository(
    private val dao: ActividadDao,
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

        withContext(Dispatchers.IO) {
            val rowId = dao.insertarActividad(entity)
            val realId = if (rowId > 0L) rowId.toInt() else entity.id

            // Programar automáticamente la alarma 30 minutos antes del inicio de la actividad
            val inicioDateTime = try {
                LocalDateTime.parse(entity.fechaInicio)
            } catch (e: Exception) {
                LocalDateTime.now().plusHours(1)
            }
            dao.insertarActividad(entity)
        }

            val fechaHoraAlarma = inicioDateTime.minusMinutes(30)

            val alarmaEntity = AlarmEntity(
                actividadId = realId,
                tituloActividad = entity.titulo,
                fechaHoraAlarma = fechaHoraAlarma.toString(),
                estaActiva = true
            )

            alarmDao?.insertarAlarma(alarmaEntity)
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
                    INSERT INTO ACTIVITY (titulo, descripcion, userId, grupoId, esConjunta, esPrivada, fechaInicio, fechaFin)
                    VALUES ('$tituloClean', '$descClean', '$userIdClean', $grupoIdVal, $esConjuntaVal, $esPrivadaVal, '${entity.fechaInicio}', '${entity.fechaFin}');
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
        }

        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val tituloClean = entity.titulo.replace("'", "''")
                val sqlDelete = "DELETE FROM ACTIVITY WHERE titulo = '$tituloClean';"
                remoteDataSource.ejecutarConsulta(apiKey, sqlDelete)
            }
        }
    }
}
