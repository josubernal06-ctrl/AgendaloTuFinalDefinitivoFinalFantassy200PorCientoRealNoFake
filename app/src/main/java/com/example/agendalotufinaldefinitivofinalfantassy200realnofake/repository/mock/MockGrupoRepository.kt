package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IGrupoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class MockGrupoRepository : IGrupoRepository {
    private val fakePropietario = Usuario(id = 1, nickname = "dev1_user", email = "dev1@agendalotu.com")
    
    private val inMemoryGrupos = MutableStateFlow(
        listOf(
            Grupo(id = 1, nombreGrupo = "Amigos de la Uni", propietario = fakePropietario, miebros = emptyList()),
            Grupo(id = 2, nombreGrupo = "Familia", propietario = fakePropietario, miebros = emptyList())
        )
    )

    override fun obtenerGrupos(): Flow<Resultado<List<Grupo>>> {
        return inMemoryGrupos.map { grupos ->
            Resultado.Exito(grupos)
        }
    }

    override suspend fun crearGrupo(grupo: Grupo) {
        delay(300)
        val current = inMemoryGrupos.value.toMutableList()
        current.add(grupo)
        inMemoryGrupos.value = current
    }

    override suspend fun eliminarGrupo(grupo: Grupo) {
        delay(300)
        val current = inMemoryGrupos.value.toMutableList()
        current.remove(grupo)
        inMemoryGrupos.value = current
    }
}
