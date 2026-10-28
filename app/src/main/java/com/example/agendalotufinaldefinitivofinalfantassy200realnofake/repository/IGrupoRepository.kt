package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import kotlinx.coroutines.flow.Flow

interface IGrupoRepository {
    fun obtenerGrupos(userId: Int): Flow<Resultado<List<Grupo>>>
    suspend fun crearGrupo(grupo: Grupo)
    suspend fun unirseAGrupo(codigo: String, user: Usuario): Resultado<Grupo>
    suspend fun eliminarGrupo(grupo: Grupo)
}
