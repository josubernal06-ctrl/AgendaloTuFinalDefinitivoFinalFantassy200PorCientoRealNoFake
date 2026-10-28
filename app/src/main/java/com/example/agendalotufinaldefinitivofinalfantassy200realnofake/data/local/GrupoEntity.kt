package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grupos")
data class GrupoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreGrupo: String,
    val propietarioId: Int,
    val propietarioNickname: String,
    val propietarioEmail: String
)
