package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {
    @Query("SELECT * FROM actividades WHERE propietarioId = :propietarioId OR grupoId IN (SELECT grupoId FROM GROUP_MEMBER)")
    fun obtenerTodasLasActividades(propietarioId: Int? = null): Flow<List<ActividadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarActividad(actividad: ActividadEntity): Long

    @Update
    fun actualizarActividad(actividad: ActividadEntity)

    @Delete
    fun eliminarActividad(actividad: ActividadEntity)
}
