package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarmas ORDER BY fechaHoraAlarma ASC")
    fun obtenerTodasLasAlarmas(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarmas WHERE estaActiva = 1")
    suspend fun obtenerAlarmasActivas(): List<AlarmEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarAlarma(alarma: AlarmEntity): Long

    @Update
    suspend fun actualizarAlarma(alarma: AlarmEntity)

    @Delete
    suspend fun eliminarAlarma(alarma: AlarmEntity)

    @Query("DELETE FROM alarmas WHERE id = :id")
    suspend fun eliminarAlarmaPorId(id: Int)

    @Query("DELETE FROM alarmas WHERE actividadId = :actividadId")
    suspend fun eliminarAlarmaPorActividadId(actividadId: Int)
}
