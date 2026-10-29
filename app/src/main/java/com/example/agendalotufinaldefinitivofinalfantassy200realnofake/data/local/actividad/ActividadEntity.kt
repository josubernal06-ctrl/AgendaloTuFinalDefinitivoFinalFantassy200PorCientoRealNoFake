package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.actividad

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val esPrivada: Boolean,
    val propietarioId: Int,
    val propietarioNickname: String,
    val inicio: String,
    val fin: String,
    val esConjunta: Boolean,
    val puntuacion: Double
)
