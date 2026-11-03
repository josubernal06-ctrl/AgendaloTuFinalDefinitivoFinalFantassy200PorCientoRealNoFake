package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {
    @Query("SELECT * FROM `GROUP`")
    fun obtenerTodosLosGrupos(): Flow<List<GrupoEntity>>

    @Query("SELECT * FROM `GROUP` WHERE propietarioId = :userId OR id IN (SELECT grupoId FROM `GROUP_MEMBER` WHERE userId = :userId)")
    fun obtenerGruposPorUsuario(userId: String): Flow<List<GrupoEntity>>

    @Query("SELECT * FROM `GROUP` WHERE id = :id")
    fun obtenerGrupoPorId(id: Int): Flow<GrupoEntity?>

    @Query("SELECT * FROM `GROUP` WHERE codigoInvitacion = :codigo LIMIT 1")
    suspend fun obtenerGrupoPorCodigoSync(codigo: String): GrupoEntity?

    @Query("SELECT * FROM `GROUP` WHERE id = :id LIMIT 1")
    suspend fun obtenerGrupoPorIdSync(id: Int): GrupoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGrupo(grupo: GrupoEntity): Long

    @Delete
    suspend fun eliminarGrupo(grupo: GrupoEntity)

    @Delete
    suspend fun eliminarMiembro(miembro: MiembroGrupoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMiembro(miembro: MiembroGrupoEntity)

    @Query("SELECT * FROM `GROUP_MEMBER` WHERE grupoId = :grupoId")
    fun obtenerMiembrosDeGrupo(grupoId: Int): Flow<List<MiembroGrupoEntity>>

    @Query("SELECT * FROM `GROUP_MEMBER` WHERE grupoId = :grupoId")
    suspend fun obtenerMiembrosDeGrupoSync(grupoId: Int): List<MiembroGrupoEntity>
}
