package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.MiembroGrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.SqliteCloudDataSource
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IGrupoRepository
import com.google.firebase.auth.FirebaseAuth
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
            val rowId = dao.insertarGrupo(entity)
            val realId = if (rowId > 0L) rowId.toInt() else entity.id
            // Agregar al creador como miembro localmente
            val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: grupo.propietario.email
            dao.insertarMiembro(MiembroGrupoEntity(grupoId = realId, userId = currentUid))
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
            val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: user.email.ifEmpty { user.id.toString() }
            val cleanCodigo = codigo.trim()
            val cleanUserId = firebaseUid.replace("'", "''")
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY

            // 1. Buscar el grupo real en SQLite Cloud por código de invitación
            var remoteGroupEntity: GrupoEntity? = null
            if (apiKey.isNotBlank()) {
                remoteGroupEntity = remoteDataSource.obtenerGrupoPorCodigoRemoto(apiKey, cleanCodigo)
            }

            if (remoteGroupEntity == null) {
                return Resultado.Error(RuntimeException("No se encontró ningún grupo con el código: $codigo"))
            }

            // 2. Unir miembro en SQLite Cloud a ese grupo específico
            withContext(Dispatchers.IO + NonCancellable) {
                if (apiKey.isNotBlank()) {
                    val sqlInsertMember = """
                        INSERT OR IGNORE INTO `GROUP_MEMBER` (grupoId, userId) 
                        VALUES (${remoteGroupEntity.id}, '$cleanUserId');
                    """.trimIndent()
                    remoteDataSource.ejecutarConsulta(apiKey, sqlInsertMember)
                }
            }

            // 3. Guardar el grupo real y la membresía localmente en Room
            val grupoDomain = withContext(Dispatchers.IO) {
                dao.insertarGrupo(remoteGroupEntity)
                dao.insertarMiembro(MiembroGrupoEntity(grupoId = remoteGroupEntity.id, userId = cleanUserId))
                remoteGroupEntity.toDomain()
            }

            Resultado.Exito(grupoDomain)
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
