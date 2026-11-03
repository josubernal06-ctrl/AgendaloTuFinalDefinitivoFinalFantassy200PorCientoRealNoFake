package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {
    @Query("SELECT * FROM actividades WHERE (:propietarioId IS NULL OR :propietarioId = 0 OR propietarioId = :propietarioId)")
    fun obtenerTodasLasActividades(propietarioId: Int? = null): Flow<List<ActividadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarActividad(actividad: ActividadEntity)

    @Update
    fun actualizarActividad(actividad: ActividadEntity)

    @Delete
    fun eliminarActividad(actividad: ActividadEntity)
}
