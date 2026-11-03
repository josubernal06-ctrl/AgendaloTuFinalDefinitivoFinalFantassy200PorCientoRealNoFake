package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.MiembroGrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.SqliteCloudDataSource
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IGrupoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GrupoRepository(
    private val dao: GrupoDao,
    private val remoteDataSource: SqliteCloudDataSource = SqliteCloudDataSource()
) : IGrupoRepository {

    override fun obtenerGrupos(userId: Int): Flow<Resultado<List<Grupo>>> {
        return dao.obtenerTodosLosGrupos().map { entities ->
            Resultado.Exito(entities.map { it.toDomain() }) as Resultado<List<Grupo>>
        }.catch { e ->
            emit(Resultado.Error(e))
        }
    }

    override suspend fun crearGrupo(grupo: Grupo) {
        if (grupo.codigoInvitacion.isBlank()) {
            grupo.codigoInvitacion = "GRP" + (100000..999999).random()
        }
        val entity = grupo.toEntity()

        withContext(Dispatchers.IO) {
            dao.insertarGrupo(entity)
        }

        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val nombreClean = entity.nombreGrupo.replace("'", "''")
                val descClean = entity.descripcion.replace("'", "''")
                val ownerClean = entity.propietarioId.replace("'", "''")
                val codigoClean = entity.codigoInvitacion.replace("'", "''")

                val sqlInsert = """
                    INSERT INTO `GROUP` (nombreGrupo, descripcion, propietarioId, codigoInvitacion)
                    VALUES ('$nombreClean', '$descClean', '$ownerClean', '$codigoClean');
                """.trimIndent()

                remoteDataSource.ejecutarConsulta(apiKey, sqlInsert)
            }
        }
    }

    override suspend fun unirseAGrupo(codigo: String, user: Usuario): Resultado<Grupo> {
        return try {
            val userClean = user.id.toString()
            val sqlInsert = "INSERT INTO `GROUP_MEMBER` (grupoId, userId) VALUES (1, '$userClean');"
            withContext(Dispatchers.IO + NonCancellable) {
                val apiKey = BuildConfig.SQLITE_CLOUD_KEY
                if (apiKey.isNotBlank()) {
                    remoteDataSource.ejecutarConsulta(apiKey, sqlInsert)
                }
            }
            Resultado.Exito(Grupo(id = 1, nombreGrupo = "Grupo Unido", propietario = user, codigoInvitacion = codigo))
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    override suspend fun eliminarGrupo(grupo: Grupo) {
        val entity = grupo.toEntity()

        withContext(Dispatchers.IO) {
            dao.eliminarGrupo(entity)
        }

        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val nombreClean = entity.nombreGrupo.replace("'", "''")
                val sqlDelete = "DELETE FROM `GROUP` WHERE nombreGrupo = '$nombreClean';"
                remoteDataSource.ejecutarConsulta(apiKey, sqlDelete)
            }
        }
    }
}
