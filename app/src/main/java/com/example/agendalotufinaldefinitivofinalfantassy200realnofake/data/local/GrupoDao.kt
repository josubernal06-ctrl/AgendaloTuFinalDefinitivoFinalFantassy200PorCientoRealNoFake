package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GrupoDao {
    @Query("SELECT * FROM grupos")
    fun obtenerTodosLosGrupos(): Flow<List<GrupoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarGrupo(grupo: GrupoEntity)

    @Delete
    suspend fun eliminarGrupo(grupo: GrupoEntity)
}
