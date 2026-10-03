package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.logica

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Conflicto
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.RecomendacionHorario
import kotlin.collections.emptyList

class GestorHorarios {
    fun calcularMejorHorario(g: Grupo, a: AbstractActividad, dur: Int): (RecomendacionHorario, List<Conflicto>) -> Pair<RecomendacionHorario?, List<Conflicto>> {
        // Extraer las PreferenciasUsuario de todos los miembros del Grupo g mas bonito
        val listaPreferenciasUsuarios = g.miembros.map { it.preferencias }

        // Encontrar coincidencias (matches) basados en igualdad o contención
        val matchesEncontrados = findMatches(listaPreferenciasUsuarios, dur)

        // Identificar cruces y generar objetos Conflicto si los hay
        val conflictos = mutableListOf<Conflicto>()
        // TODO: Lógica adicional para verificar cruces con actividades existentes

        // 4. Construir la recomendación final si hay matches
        val mejorSlot = matchesEncontrados.firstOrNull()
        val alternativas = if (matchesEncontrados.size > 1) matchesEncontrados.drop(1) else emptyList()

        val recomendacionFinal = if (mejorSlot != null) {
            RecomendacionHorario(mejorSlot, alternativas, conflictos)
        } else {
            null
        }

        return Pair(recomendacionFinal, conflictos)
    }

    fun findMatches(listaPreferenciasUsuarios: List<PreferenciasUsuario>, dur: Int): List<RecomendacionHorario> {

    }
}