package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import kotlinx.coroutines.flow.Flow

interface IGrupoRepository {
    fun obtenerGrupos(): Flow<Resultado<List<Grupo>>>
    suspend fun crearGrupo(grupo: Grupo)
    suspend fun eliminarGrupo(grupo: Grupo)
}
