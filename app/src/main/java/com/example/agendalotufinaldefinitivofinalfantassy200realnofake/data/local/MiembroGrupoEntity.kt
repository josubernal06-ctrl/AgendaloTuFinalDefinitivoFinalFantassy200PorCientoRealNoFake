package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.Entity

@Entity(tableName = "GROUP_MEMBER", primaryKeys = ["grupoId", "userId"])
data class MiembroGrupoEntity(
    val grupoId: Int,
    val userId: String
)
