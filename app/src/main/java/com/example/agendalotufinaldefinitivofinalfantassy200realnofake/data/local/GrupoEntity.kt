package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GROUP")
data class GrupoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreGrupo: String,
    val descripcion: String = "",
    val propietarioId: String,
    val codigoInvitacion: String = ""
)
