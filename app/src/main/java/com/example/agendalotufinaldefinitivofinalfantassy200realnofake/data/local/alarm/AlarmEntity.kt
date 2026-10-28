package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarmas")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val actividadId: Int,
    val tituloActividad: String,
    val fechaHoraAlarma: String, // Formato ISO-8601 (yyyy-MM-ddTHH:mm:ss)
    val estaActiva: Boolean = true
)
