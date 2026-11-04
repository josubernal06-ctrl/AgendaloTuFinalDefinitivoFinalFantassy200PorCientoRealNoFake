package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.logica

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

/**
 * LÓGICA DE NEGOCIO Y PROCESAMIENTO INTELIGENTE DE HORARIOS CON GEMINI AI.
 * Contiene helpers para construir prompts formateados en JSON y deserializar
 * las respuestas del modelo generativo en instancias de Actividad.
 */
class GestorHorarios {
    fun calcularMejorHorario(g: Grupo, a: AbstractActividad, dur: Int): RecomendacionHorario? {
        return null
    }

    companion object {
        /**
         * Construye un prompt enriquecido con la fecha/hora actual en español
         * e instruye a Gemini AI para que responda estrictamente en un objeto JSON.
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
                Interpreta la solicitud del usuario y genera la información estructurada de la ACTIVIDAD que desea agendar en su horario o en conjunto con su grupo (evitando conflictos con otros miembros si se indica).
                Debes responder ÚNICAMENTE con un objeto JSON válido, sin bloques de código markdown ```json, sin explicaciones ni ningún texto adicional.

                ESTRUCTURA JSON REQUERIDA:
                {
                  "titulo": "Título o nombre corto de la actividad",
                  "descripcion": "Descripción o detalles",
                  "inicio": "YYYY-MM-DDTHH:mm:ss",
                  "fin": "YYYY-MM-DDTHH:mm:ss",
                  "esConjunta": true,
                  "esPrivada": false
                }

                REGLAS DE INTERPRETACIÓN:
                1. "inicio" y "fin" DEBEN ser fechas ISO-8601 válidas (formato: YYYY-MM-DDTHH:mm:ss).
                2. Si el usuario menciona días relativos como "el viernes", "mañana", "hoy", "este lunes", calcula la fecha correspondiente basada en HOY ($fechaHoyStr).
                3. Si el usuario especifica horas como "de 3 a 4" o "de 15 a 16", asume el horario razonable (ej. 15:00:00 a 16:00:00).
                4. Si no especifica hora de fin, asume 1 hora de duración posterior a la hora de inicio.

                Solicitud del usuario: "$mensajeUsuario"
            """.trimIndent()
        }

        /**
         * Limpia bloques markdown y deserializa el JSON devuelto por la IA
         * convirtiéndolo en un objeto de dominio ActividadConjunta o ActividadSolitaria.
         */
        fun parsearActividadDesdeRespuesta(respuestaApi: String, usuarioActual: Usuario? = null, grupoId: Int? = null): AbstractActividad? {
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
                val esConjunta = jsonObject.optBoolean("esConjunta", false) || grupoId != null
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

                if (esConjunta || grupoId != null) {
                    ActividadConjunta(
                        id = idGenerado,
                        titulo = titulo,
                        descripcion = descripcion,
                        propietario = propietario,
                        esPrivada = esPrivada,
                        segmentoTiempo = segmento,
                        grupoId = grupoId ?: -1
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
                null
            }
        }

        /** Helper seguro para intentar parsear varias máscaras de fecha ISO-8601. */
        private fun parsearFecha(fechaStr: String): LocalDateTime {
            return try {
                LocalDateTime.parse(fechaStr)
            } catch (e: Exception) {
                try {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    LocalDateTime.parse(fechaStr.replace("T", " "), formatter)
                } catch (ex: Exception) {
                    LocalDateTime.now().plusHours(1)
                }
            }
        }
    }
}
