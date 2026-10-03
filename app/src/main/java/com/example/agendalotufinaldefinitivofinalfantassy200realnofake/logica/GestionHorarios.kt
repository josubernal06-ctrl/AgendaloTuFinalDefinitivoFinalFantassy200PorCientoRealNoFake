package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.logica

import android.util.Log.i
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Conflicto
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.RecomendacionHorario

class GestorHorarios {
    fun calcularMejorHorario(g: Grupo, a: AbstractActividad, dur: Int): (RecomendacionHorario, List<Conflicto>) {
        // TODO: Extraer las PreferenciasUsuario de todos los miembros del Grupo g
        val prefs = mutableListOf<Any>()
        for (miembro in g.miembros) {
            prefs.add(miembro.preferencias)
        }

        // TODO: Comparar las disponibilidades para encontrar coincidencias
        val recomendacionFinal = findMatches(prefs, dur)
        // TODO: Identificar cruces y generar objetos Conflicto si los hay


        return recomendacionFinal, conflictos
    }

    fun findMatches(prefs: List<Any>, dur: Int): List<RecomendacionHorario> {

        //lista vaacia de matches
        val matches = mutableListOf<Any>()
        for (preferenciasUser in prefs) {

        }

        return matches
    }
}