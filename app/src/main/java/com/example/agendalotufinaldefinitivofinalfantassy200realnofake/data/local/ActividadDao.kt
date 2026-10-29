package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ActividadDao {
    @Query("SELECT * FROM actividades")
    fun obtenerTodasLasActividades(): Flow<List<ActividadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarActividad(actividad: ActividadEntity): Long

    @Update
    fun actualizarActividad(actividad: ActividadEntity)

    @Delete
    fun eliminarActividad(actividad: ActividadEntity)
}
