package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val userId: String,
    val grupoId: Int? = null,
    val esConjunta: Boolean,
    val esPrivada: Boolean,
    val fechaInicio: String,
    val fechaFin: String
)
