package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {
    @Query("SELECT * FROM actividades WHERE (grupoId IS NULL AND propietarioId = :propietarioId) OR (grupoId IS NOT NULL AND grupoId IN (SELECT grupoId FROM GROUP_MEMBER WHERE userId = :userUid))")
    fun obtenerTodasLasActividades(propietarioId: Int, userUid: String): Flow<List<ActividadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarActividad(actividad: ActividadEntity): Long

    @Update
    fun actualizarActividad(actividad: ActividadEntity)

    @Delete
    fun eliminarActividad(actividad: ActividadEntity)
}
