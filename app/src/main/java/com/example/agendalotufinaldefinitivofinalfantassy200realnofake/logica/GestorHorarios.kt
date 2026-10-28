package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.logica

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class GestorHorarios {
    fun calcularMejorHorario(g: Grupo, a: AbstractActividad, dur: Int): RecomendacionHorario? {
        // Implementación dummy para compilar y cumplir con el diagrama
        return null
    }

    companion object {
        /**
         * Concatena el mensaje del usuario con un prompt optimizado y especializado
         * en la creación de ACTIVIDADES para el horario del usuario.
         */
        fun construirPromptActividad(mensajeUsuario: String): String {
            val ahora = LocalDateTime.now()
            val diaSemanaHoy = ahora.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
            val fechaHoyStr = ahora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

            return """
                Eres un asistente inteligente de agenda para la aplicación AgendaloTu.
                INFORMACIÓN DE CONTEXTO TEMPORAL:
                Hoy es $diaSemanaHoy, $fechaHoyStr.

                TU TAREA:
                Interpreta la solicitud del usuario y genera la información estructurada de la ACTIVIDAD que desea agendar en su horario.
                Debes responder ÚNICAMENTE con un objeto JSON válido, sin bloques de código markdown ```json, sin explicaciones ni ningún texto adicional.

                ESTRUCTURA JSON REQUERIDA:
                {
                  "titulo": "Título o nombre corto de la actividad",
                  "descripcion": "Descripción o detalles",
                  "inicio": "YYYY-MM-DDTHH:mm:ss",
                  "fin": "YYYY-MM-DDTHH:mm:ss",
                  "esConjunta": false,
                  "esPrivada": false
                }

                REGLAS DE INTERPRETACIONAL:
                1. "inicio" y "fin" DEBEN ser fechas ISO-8601 válidas (formato: YYYY-MM-DDTHH:mm:ss).
                2. Si el usuario menciona días relativos como "el viernes", "mañana", "hoy", "este lunes", calcula la fecha correspondiente basada en HOY ($fechaHoyStr).
                3. Si el usuario especifica horas como "de 3 a 4" o "de 15 a 16", asume el horario razonable (ej. 15:00:00 a 16:00:00).
                4. Si no especifica hora de fin, asume 1 hora de duración posterior a la hora de inicio.

                Solicitud del usuario: "$mensajeUsuario"
            """.trimIndent()
        }

        /**
         * Procesa el texto de respuesta de la API de Gemini y lo convierte en un objeto AbstractActividad.
         */
        fun parsearActividadDesdeRespuesta(respuestaApi: String, usuarioActual: Usuario? = null): AbstractActividad? {
            return try {
                var jsonLimpio = respuestaApi.trim()
                if (jsonLimpio.startsWith("```json")) {
                    jsonLimpio = jsonLimpio.removePrefix("```json").trim()
                } else if (jsonLimpio.startsWith("```")) {
                    jsonLimpio = jsonLimpio.removePrefix("```").trim()
                }
                if (jsonLimpio.endsWith("```")) {
                    jsonLimpio = jsonLimpio.removeSuffix("```").trim()
                }

                val primerBrace = jsonLimpio.indexOf('{')
                val ultimoBrace = jsonLimpio.lastIndexOf('}')
                if (primerBrace != -1 && ultimoBrace != -1 && ultimoBrace > primerBrace) {
                    jsonLimpio = jsonLimpio.substring(primerBrace, ultimoBrace + 1)
                }

                val jsonObject = JSONObject(jsonLimpio)
                val titulo = jsonObject.optString("titulo", "Nueva Actividad").ifBlank { "Nueva Actividad" }
                val descripcion = jsonObject.optString("descripcion", "Creado con Gemini AI")
                val inicioStr = jsonObject.optString("inicio", "")
                val finStr = jsonObject.optString("fin", "")
                val esConjunta = jsonObject.optBoolean("esConjunta", false)
                val esPrivada = jsonObject.optBoolean("esPrivada", false)

                val inicio = parsearFecha(inicioStr)
                val fin = if (finStr.isNotBlank()) parsearFecha(finStr) else inicio.plusHours(1)

                val segmento = SegmentoTiempo(
                    inicio = inicio,
                    fin = if (fin.isAfter(inicio)) fin else inicio.plusHours(1),
                    esConjunta = esConjunta,
                    puntuacion = 1.0
                )

                val propietario = usuarioActual ?: Usuario(
                    id = 1,
                    nickname = "Usuario",
                    email = "usuario@agendalotu.com"
                )

                val idGenerado = (System.currentTimeMillis() % 100000).toInt()

                if (esConjunta) {
                    ActividadConjunta(
                        id = idGenerado,
                        titulo = titulo,
                        descripcion = descripcion,
                        propietario = propietario,
                        esPrivada = esPrivada,
                        segmentoTiempo = segmento
                    )
                } else {
                    ActividadSolitaria(
                        id = idGenerado,
                        titulo = titulo,
                        descripcion = descripcion,
                        propietario = propietario,
                        esPrivada = esPrivada,
                        segmentoTiempo = segmento
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        private fun parsearFecha(fechaStr: String): LocalDateTime {
            if (fechaStr.isBlank()) return LocalDateTime.now().plusHours(1)
            return try {
                LocalDateTime.parse(fechaStr)
            } catch (e: Exception) {
                try {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm[:ss]")
                    LocalDateTime.parse(fechaStr, formatter)
                } catch (e2: Exception) {
                    try {
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm[:ss]")
                        LocalDateTime.parse(fechaStr, formatter)
                    } catch (e3: Exception) {
                        LocalDateTime.now().plusHours(1)
                    }
                }
            }
        }
    }
}

