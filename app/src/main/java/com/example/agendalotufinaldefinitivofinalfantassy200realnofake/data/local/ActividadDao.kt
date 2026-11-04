package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) DE ACTIVIDADES PARA ROOM DATABASE.
 * Define las consultas y operaciones de lectura/escritura en SQLite local.
 */
@Dao
interface ActividadDao {

    /**
     * Consulta con filtro de autorización de datos:
     * - Muestra actividades personales (grupoId IS NULL) si pertenecen al propietarioId actual.
     * - Muestra actividades grupales (grupoId IS NOT NULL) si el grupoId pertenece a la lista de grupos del userUid actual.
     */
    @Query("SELECT * FROM actividades WHERE (grupoId IS NULL AND propietarioId = :propietarioId) OR (grupoId IS NOT NULL AND grupoId IN (SELECT grupoId FROM GROUP_MEMBER WHERE userId = :userUid))")
    fun obtenerTodasLasActividades(propietarioId: Int, userUid: String): Flow<List<ActividadEntity>>

    /** Inserta o actualiza una actividad en la base de datos local. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertarActividad(actividad: ActividadEntity): Long

    /** Actualiza una actividad existente. */
    @Update
    fun actualizarActividad(actividad: ActividadEntity)

    /** Elimina una actividad de la base de datos local. */
    @Delete
    fun eliminarActividad(actividad: ActividadEntity)
}
