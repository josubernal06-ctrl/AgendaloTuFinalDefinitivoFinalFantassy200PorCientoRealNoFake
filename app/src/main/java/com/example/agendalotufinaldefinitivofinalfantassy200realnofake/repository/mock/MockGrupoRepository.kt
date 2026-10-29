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
            Grupo(id = 1, nombreGrupo = "Amigos de la Uni", propietario = fakePropietario, descripcion = "Grupo para estudios", codigoInvitacion = "UNI123", miebros = listOf(fakePropietario)),
            Grupo(id = 2, nombreGrupo = "Familia", propietario = fakePropietario, descripcion = "Actividades familiares", codigoInvitacion = "FAM456", miebros = listOf(fakePropietario))
        )
    )

    override fun obtenerGrupos(userId: Int): Flow<Resultado<List<Grupo>>> {
        // En este mock simplificado, devolvemos los grupos de los que el usuario es miembro
        return inMemoryGrupos.map { grupos ->
            val gruposDelUsuario = grupos.filter { grupo -> 
                grupo.miebros.any { it.id == userId } 
            }
            Resultado.Exito(gruposDelUsuario)
        }
    }

    override suspend fun unirseAGrupo(codigo: String, user: Usuario): Resultado<Grupo> {
        delay(500)
        val normalizedCode = codigo.trim().uppercase()
        val current = inMemoryGrupos.value.toMutableList()
        val grupoIndex = current.indexOfFirst { it.codigoInvitacion == normalizedCode }
        
        return if (grupoIndex != -1) {
            val grupo = current[grupoIndex]
            if (grupo.miebros.any { it.email == user.email }) {
                Resultado.Error(Exception("Ya perteneces a este grupo"))
            } else {
                grupo.agregarMiembro(user)
                inMemoryGrupos.value = current
                Resultado.Exito(grupo)
            }
        } else {
            Resultado.Error(Exception("Código de invitación inválido o no existe"))
        }
    }

    override suspend fun crearGrupo(grupo: Grupo) {
        delay(300)
        val current = inMemoryGrupos.value.toMutableList()
        // Generate a simple unique code
        if (grupo.codigoInvitacion.isBlank()) {
            grupo.codigoInvitacion = "GRP${(current.size + 1)}${(100..999).random()}"
        }
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
