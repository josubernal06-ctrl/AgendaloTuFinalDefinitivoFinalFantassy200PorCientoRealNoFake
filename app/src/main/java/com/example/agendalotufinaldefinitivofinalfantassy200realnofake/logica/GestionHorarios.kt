package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.logica

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Conflicto
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.PreferenciasUsuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.RecomendacionHorario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.SegmentoTiempo
import java.time.Duration
import java.time.LocalDateTime

class GestorHorarios {

    // Función principal para calcular el mejor horario del grupo
    fun calcularMejorHorario(g: Grupo, a: AbstractActividad, dur: Int): Pair<RecomendacionHorario?, List<Conflicto>> {

        // 1. Extraer las PreferenciasUsuario de todos los miembros del Grupo g
        val listaPreferenciasUsuarios = g.miembros.map { it.preferencias }

        // 2. Encontrar coincidencias (matches) basados en igualdad o contención
        val matchesEncontrados = findMatches(listaPreferenciasUsuarios, dur)

        // 3. Identificar cruces y generar objetos Conflicto si los hay
        val conflictos = mutableListOf<Conflicto>()

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

    // Función para buscar coincidencias entre las preferencias de los miembros
    fun findMatches(prefs: List<PreferenciasUsuario>, dur: Int): List<SegmentoTiempo> {
        val matches = mutableListOf<SegmentoTiempo>()
        if (prefs.isEmpty()) return matches

        // Tomamos las preferencias del primer usuaroi como base para comparar con los demás
        val basePrefs = prefs.first().listaPreferencias

        for (p1 in basePrefs) {
            // Verificar si todos los demás miembros tambien tienen compatibilidad en este día y horario
            var esValidoParaTodos = true

            for (otherPrefs in prefs.drop(1)) {
                val coincideConEsteMiembro = otherPrefs.listaPreferencias.any { p2 ->
                    // Mismo día obviamente
                    if (p1.dia == p2.dia) {
                        // Coincidencia exacta de hora de inicio y fin (raro pero es posible creo)
                        val esIdentico = p1.horaInicio == p2.horaInicio && p1.horaFin == p2.horaFin

                        // Contención (una franja está dentro de la otra)
                        val estaContenido = (p2.horaInicio <= p1.horaInicio && p1.horaFin <= p2.horaFin) ||
                                (p1.horaInicio <= p2.horaInicio && p2.horaFin <= p1.horaFin)

                        esIdentico || estaContenido
                    } else {
                        false
                    }
                }
                if (!coincideConEsteMiembro) {
                    esValidoParaTodos = false
                    break
                }
            }

            // Si es valido para todos los miembros del grupo, validamos la duracion
            if (esValidoParaTodos) {
                val duracionMinutos = Duration.between(p1.horaInicio, p1.horaFin).toMinutes()
                if (duracionMinutos >= dur) {
                    val fechaHoy = LocalDateTime.now().toLocalDate()
                    val inicioLdt = LocalDateTime.of(fechaHoy, p1.horaInicio)
                    val finLdt = LocalDateTime.of(fechaHoy, p1.horaFin)

                    matches.add(
                        SegmentoTiempo(
                            dayOfWeek = p1.dia,
                            inicio = inicioLdt,
                            fin = finLdt,
                            esConjunta = true,
                            puntuacion = p1.prioridad.toDouble()
                        )
                    )
                }
            }
        }

        return matches
    }
}