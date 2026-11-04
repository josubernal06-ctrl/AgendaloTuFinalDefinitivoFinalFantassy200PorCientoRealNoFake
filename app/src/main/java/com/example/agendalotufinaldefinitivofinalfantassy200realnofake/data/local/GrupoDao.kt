package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) DE GRUPOS Y MIEMBROS PARA ROOM DATABASE.
 * Maneja la persistencia local de grupos y sus integrantes.
 */
@Dao
interface GrupoDao {
    /** Retorna el flujo continuo de todos los grupos. */
    @Query("SELECT * FROM `GROUP`")
    fun obtenerTodosLosGrupos(): Flow<List<GrupoEntity>>

    /** Retorna los grupos pertenecientes o compartidos con el userId actual (Flow reactivo). */
    @Query("SELECT * FROM `GROUP` WHERE propietarioId = :userId OR id IN (SELECT grupoId FROM `GROUP_MEMBER` WHERE userId = :userId)")
    fun obtenerGruposPorUsuario(userId: String): Flow<List<GrupoEntity>>

    /** Retorna los grupos del userId de forma síncrona/suspend para reconciliación. */
    @Query("SELECT * FROM `GROUP` WHERE propietarioId = :userId OR id IN (SELECT grupoId FROM `GROUP_MEMBER` WHERE userId = :userId)")
    suspend fun obtenerGruposPorUsuarioSync(userId: String): List<GrupoEntity>

    /** Obtiene el grupo por su ID numérico. */
    @Query("SELECT * FROM `GROUP` WHERE id = :id")
    fun obtenerGrupoPorId(id: Int): Flow<GrupoEntity?>

    /** Busca un grupo por código de invitación de forma síncrona. */
    @Query("SELECT * FROM `GROUP` WHERE codigoInvitacion = :codigo LIMIT 1")
    suspend fun obtenerGrupoPorCodigoSync(codigo: String): GrupoEntity?

    /** Busca un grupo por ID de forma síncrona. */
    @Query("SELECT * FROM `GROUP` WHERE id = :id LIMIT 1")
    suspend fun obtenerGrupoPorIdSync(id: Int): GrupoEntity?

    /** Inserta o reemplaza un grupo en Room. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGrupo(grupo: GrupoEntity): Long

    /** Elimina un grupo de Room. */
    @Delete
    suspend fun eliminarGrupo(grupo: GrupoEntity)

    /** Elimina la entidad miembro especificada. */
    @Delete
    suspend fun eliminarMiembro(miembro: MiembroGrupoEntity)

    /** Elimina un miembro por grupoId y userId específicos. */
    @Query("DELETE FROM `GROUP_MEMBER` WHERE grupoId = :grupoId AND userId = :userId")
    suspend fun eliminarMiembroPorId(grupoId: Int, userId: String)

    /** Elimina todos los miembros de un grupo cuando se borra el grupo. */
    @Query("DELETE FROM `GROUP_MEMBER` WHERE grupoId = :grupoId")
    suspend fun eliminarTodosLosMiembrosDeGrupo(grupoId: Int)

    /** Inserta un miembro en la tabla intermedia de Room. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMiembro(miembro: MiembroGrupoEntity)

    /** Retorna el flujo de miembros de un grupo. */
    @Query("SELECT * FROM `GROUP_MEMBER` WHERE grupoId = :grupoId")
    fun obtenerMiembrosDeGrupo(grupoId: Int): Flow<List<MiembroGrupoEntity>>

    /** Retorna los miembros de un grupo de forma síncrona. */
    @Query("SELECT * FROM `GROUP_MEMBER` WHERE grupoId = :grupoId")
    suspend fun obtenerMiembrosDeGrupoSync(grupoId: Int): List<MiembroGrupoEntity>
}
