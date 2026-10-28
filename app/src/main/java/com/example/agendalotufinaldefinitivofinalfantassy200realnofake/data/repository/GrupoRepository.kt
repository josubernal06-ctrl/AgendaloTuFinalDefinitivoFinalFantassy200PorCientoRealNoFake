package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GrupoRepository(private val dao: GrupoDao) : IGrupoRepository {

    override fun obtenerGrupos(): Flow<Resultado<List<Grupo>>> {
        return dao.obtenerTodosLosGrupos().map { entities ->
            Resultado.Exito(entities.map { it.toDomain() }) as Resultado<List<Grupo>>
        }.catch { e ->
            emit(Resultado.Error(e))
        }
    }

    override suspend fun crearGrupo(grupo: Grupo) {
        withContext(Dispatchers.IO) {
            dao.insertarGrupo(grupo.toEntity())
        }
    }

    override suspend fun eliminarGrupo(grupo: Grupo) {
        withContext(Dispatchers.IO) {
            dao.eliminarGrupo(grupo.toEntity())
        }
    }
}
