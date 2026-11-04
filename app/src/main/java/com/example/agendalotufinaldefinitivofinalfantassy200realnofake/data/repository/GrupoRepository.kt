package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import android.util.Log
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.MiembroGrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.SqliteCloudDataSource
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IGrupoRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * REPOSITORIO DE GRUPOS (PATRÓN REPOSITORY).
 * Maneja la sincronización bidireccional de grupos y sus integrantes entre
 * SQLite Cloud (fuente de verdad remota) y Room (almacenamiento local reactivo).
 */
class GrupoRepository(
    private val dao: GrupoDao,
    private val remoteDataSource: SqliteCloudDataSource = SqliteCloudDataSource()
) : IGrupoRepository {

    /**
     * Retorna el flujo continuo (Flow) de grupos pertenecientes al usuario actual desde Room,
     * obteniendo y asociando los nicknames reales de cada participante desde SQLite Cloud.
     */
    override fun obtenerGrupos(userId: Int): Flow<Resultado<List<Grupo>>> {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val currentUid = firebaseUser?.uid ?: userId.toString()

        val apiKey = BuildConfig.SQLITE_CLOUD_KEY

        return dao.obtenerGruposPorUsuario(currentUid).map { entities ->
            val remoteNicknames = if (apiKey.isNotBlank()) {
                remoteDataSource.obtenerNicknamesRemotos(apiKey)
            } else {
                emptyMap()
            }

            val grupos = entities.map { entity ->
                val miembrosEntities = dao.obtenerMiembrosDeGrupoSync(entity.id)
                val usuarios = miembrosEntities.map { miembro ->
                    val uid = miembro.userId
                    val nickname = remoteNicknames[uid] ?: miembro.userId.substringBefore("@").ifEmpty { "Usuario" }
                    Usuario(
                        id = uid.hashCode(),
                        nickname = nickname,
                        email = uid
                    )
                }
                val domainGroup = entity.toDomain()
                domainGroup.miebros = usuarios
                domainGroup
            }
            Resultado.Exito(grupos) as Resultado<List<Grupo>>
        }.catch { e ->
            emit(Resultado.Error(e))
        }
    }

    /** Invoca la sincronización en segundo plano con SQLite Cloud para actualizar Room local. */
    override suspend fun sincronizarGrupos(userId: Int) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        val apiKey = BuildConfig.SQLITE_CLOUD_KEY
        if (apiKey.isNotBlank()) {
            withContext(Dispatchers.IO) {
                sincronizarGruposEIntegrantesRemotos(apiKey, firebaseUser.uid)
            }
        }
    }

    private suspend fun sincronizarGruposEIntegrantesRemotos(apiKey: String, uid: String) {
        try {
            val remoteGroups = remoteDataSource.obtenerGruposRemotosDeUsuario(apiKey, uid)
            if (remoteGroups == null) {
                Log.w("GrupoRepository", "Sincronización remota omitida debido a falla de conexión con SQLite Cloud")
                return
            }

            val remoteGroupIds = remoteGroups.map { it.id }.toSet()

            // 1. Reconciliar y borrar de Room los grupos locales que ya no existen remotamente
            val localGroups = dao.obtenerGruposPorUsuarioSync(uid)
            for (localGroup in localGroups) {
                if (!remoteGroupIds.contains(localGroup.id)) {
                    Log.d("GrupoRepository", "Eliminando grupo obsoleto de Room local: ${localGroup.nombreGrupo} (id: ${localGroup.id})")
                    dao.eliminarGrupo(localGroup)
                    dao.eliminarTodosLosMiembrosDeGrupo(localGroup.id)
                }
            }

            // 2. Insertar/actualizar grupos y miembros vigentes recibidos de SQLite Cloud
            for (remoteGroup in remoteGroups) {
                dao.insertarGrupo(remoteGroup)
                val memberUids = remoteDataSource.obtenerMiembrosDeGrupoRemoto(apiKey, remoteGroup.id)
                val allMemberUids = (memberUids + remoteGroup.propietarioId).filter { it.isNotBlank() }.distinct()
                
                // Limpiar miembros obsoletos de Room
                val miembrosLocales = dao.obtenerMiembrosDeGrupoSync(remoteGroup.id)
                for (miembroLocal in miembrosLocales) {
                    if (!allMemberUids.contains(miembroLocal.userId)) {
                        dao.eliminarMiembroPorId(remoteGroup.id, miembroLocal.userId)
                    }
                }

                for (memberUid in allMemberUids) {
                    dao.insertarMiembro(MiembroGrupoEntity(grupoId = remoteGroup.id, userId = memberUid))
                }
            }
        } catch (e: Exception) {
            Log.e("GrupoRepository", "Error sincronizando grupos remotos: ${e.message}")
        }
    }

    /**
     * Crea un grupo en SQLite Cloud, obtiene su ID autogenerado e inserta
     * al usuario creador como propietario en la tabla GROUP_MEMBER local y remota.
     */
    override suspend fun crearGrupo(grupo: Grupo) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("Debes iniciar sesión para crear un grupo")
        val currentUid = firebaseUser.uid

        if (grupo.codigoInvitacion.isBlank()) {
            grupo.codigoInvitacion = "GRP" + (100000..999999).random()
        }

        val apiKey = BuildConfig.SQLITE_CLOUD_KEY
        if (apiKey.isBlank()) {
            throw IllegalStateException("SQLITE_CLOUD_KEY vacía en la configuración")
        }

        val nombreClean = grupo.nombreGrupo.replace("'", "''")
        val descClean = grupo.descripcion.replace("'", "''")
        val ownerClean = currentUid.replace("'", "''")
        val codigoClean = grupo.codigoInvitacion.replace("'", "''")

        // 1. Insertar en SQLite Cloud
        val sqlInsertGroup = """
            INSERT INTO `GROUP` (nombreGrupo, descripcion, propietarioId, codigoInvitacion)
            VALUES ('$nombreClean', '$descClean', '$ownerClean', '$codigoClean');
        """.trimIndent()

        val exitoInsert = remoteDataSource.ejecutarConsulta(apiKey, sqlInsertGroup)
        if (!exitoInsert) {
            throw RuntimeException("Error al registrar el grupo en SQLite Cloud")
        }

        // 2. Obtener el ID asignado por SQLite Cloud
        val remoteGroup = remoteDataSource.obtenerGrupoPorCodigoRemoto(apiKey, codigoClean)
            ?: throw RuntimeException("No se pudo verificar el grupo creado en SQLite Cloud")

        // 3. Registrar al propietario en GROUP_MEMBER en SQLite Cloud
        val sqlInsertMember = """
            INSERT OR IGNORE INTO `GROUP_MEMBER` (grupoId, userId) 
            VALUES (${remoteGroup.id}, '$ownerClean');
        """.trimIndent()
        remoteDataSource.ejecutarConsulta(apiKey, sqlInsertMember)

        // 4. Guardar localmente en Room con el ID real de SQLite Cloud
        withContext(Dispatchers.IO) {
            dao.insertarGrupo(remoteGroup)
            dao.insertarMiembro(MiembroGrupoEntity(grupoId = remoteGroup.id, userId = currentUid))
        }
    }

    /**
     * Valida un código de invitación contra SQLite Cloud y registra al usuario actual
     * en la tabla GROUP_MEMBER remota y local.
     */
    override suspend fun unirseAGrupo(codigo: String, user: Usuario): Resultado<Grupo> {
        return try {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
                ?: return Resultado.Error(IllegalStateException("Debes iniciar sesión para unirte a un grupo"))
            val cleanUserId = firebaseUser.uid.replace("'", "''")

            val cleanCodigo = codigo.trim().uppercase()
            if (cleanCodigo.isBlank()) {
                return Resultado.Error(IllegalArgumentException("El código de invitación no puede estar vacío"))
            }

            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isBlank()) {
                return Resultado.Error(IllegalStateException("Error de configuración de red (SQLITE_CLOUD_KEY vacía)"))
            }

            // 1. Buscar grupo en SQLite Cloud por código de invitación
            val remoteGroupEntity = remoteDataSource.obtenerGrupoPorCodigoRemoto(apiKey, cleanCodigo)
                ?: return Resultado.Error(RuntimeException("No existe un grupo con el código: $cleanCodigo"))

            // 2. Verificar si el usuario ya pertenece al grupo
            val miembrosLocales = withContext(Dispatchers.IO) { dao.obtenerMiembrosDeGrupoSync(remoteGroupEntity.id) }
            val miembrosRemotos = remoteDataSource.obtenerMiembrosDeGrupoRemoto(apiKey, remoteGroupEntity.id)
            val yaEsMiembro = miembrosLocales.any { it.userId == firebaseUser.uid } || miembrosRemotos.contains(firebaseUser.uid)

            if (yaEsMiembro) {
                withContext(Dispatchers.IO) {
                    dao.insertarGrupo(remoteGroupEntity)
                    dao.insertarMiembro(MiembroGrupoEntity(grupoId = remoteGroupEntity.id, userId = firebaseUser.uid))
                }
                return Resultado.Error(IllegalStateException("Ya perteneces a este grupo"))
            }

            // 3. Insertar membresía en SQLite Cloud
            val sqlInsertMember = """
                INSERT OR IGNORE INTO `GROUP_MEMBER` (grupoId, userId) 
                VALUES (${remoteGroupEntity.id}, '$cleanUserId');
            """.trimIndent()
            val exitoMiembro = remoteDataSource.ejecutarConsulta(apiKey, sqlInsertMember)

            if (!exitoMiembro) {
                return Resultado.Error(RuntimeException("Error al registrar la membresía en SQLite Cloud"))
            }

            // 4. Traer integrantes e insertar en Room local
            val todosMiembrosUids = (miembrosRemotos + firebaseUser.uid + remoteGroupEntity.propietarioId).distinct()
            val remoteNicknames = remoteDataSource.obtenerNicknamesRemotos(apiKey)

            withContext(Dispatchers.IO) {
                dao.insertarGrupo(remoteGroupEntity)
                for (memberUid in todosMiembrosUids) {
                    dao.insertarMiembro(MiembroGrupoEntity(grupoId = remoteGroupEntity.id, userId = memberUid))
                }
            }

            val usuariosDomain = todosMiembrosUids.map { uid ->
                val nickname = remoteNicknames[uid] ?: uid.substringBefore("@").ifEmpty { "Usuario" }
                Usuario(
                    id = uid.hashCode(),
                    nickname = nickname,
                    email = uid
                )
            }

            val domainGroup = remoteGroupEntity.toDomain()
            domainGroup.miebros = usuariosDomain

            Resultado.Exito(domainGroup)
        } catch (e: Exception) {
            Resultado.Error(e)
        }
    }

    /** Valida permisos de propietario y elimina el grupo y sus membresías en Room y SQLite Cloud. */
    override suspend fun eliminarGrupo(grupo: Grupo) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("Debes iniciar sesión para eliminar un grupo")

        val currentUid = firebaseUser.uid
        if (grupo.propietario.email != currentUid && grupo.propietario.id.toString() != currentUid) {
            throw IllegalStateException("Solo el propietario puede eliminar el grupo")
        }

        val entity = grupo.toEntity()

        withContext(Dispatchers.IO) {
            dao.eliminarGrupo(entity)
            dao.eliminarMiembroPorId(entity.id, currentUid)
        }

        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val nombreClean = entity.nombreGrupo.replace("'", "''")
                val sqlDelete = "DELETE FROM `GROUP` WHERE id = ${entity.id} OR (nombreGrupo = '$nombreClean' AND propietarioId = '$currentUid');"
                remoteDataSource.ejecutarConsulta(apiKey, sqlDelete)

                val sqlDeleteMembers = "DELETE FROM `GROUP_MEMBER` WHERE grupoId = ${entity.id};"
                remoteDataSource.ejecutarConsulta(apiKey, sqlDeleteMembers)
            }
        }
    }

    /** Elimina únicamente la membresía del usuario actual en Room y en GROUP_MEMBER de SQLite Cloud. */
    override suspend fun salirDelGrupo(grupo: Grupo) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("Debes iniciar sesión para salir de un grupo")
            
        val currentUid = firebaseUser.uid
        if (grupo.propietario.email == currentUid || grupo.propietario.id.toString() == currentUid) {
            throw IllegalStateException("Eres el propietario del grupo. Primero debes transferir la propiedad o eliminar el grupo.")
        }

        withContext(Dispatchers.IO) {
            dao.eliminarMiembroPorId(grupo.id, currentUid)
        }

        withContext(Dispatchers.IO + NonCancellable) {
            val apiKey = BuildConfig.SQLITE_CLOUD_KEY
            if (apiKey.isNotBlank()) {
                val currentUidClean = currentUid.replace("'", "''")
                val sqlDeleteMember = "DELETE FROM `GROUP_MEMBER` WHERE grupoId = ${grupo.id} AND userId = '$currentUidClean';"
                remoteDataSource.ejecutarConsulta(apiKey, sqlDeleteMember)
            }
        }
    }
}
