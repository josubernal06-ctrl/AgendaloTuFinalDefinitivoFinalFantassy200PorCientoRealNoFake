package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.RetrofitClient
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.BuildConfig
import kotlinx.coroutines.delay

class AiRepository {
    // Obtenemos la API_KEY inyectada desde local.properties a través de BuildConfig
    private val API_KEY = BuildConfig.GEMINI_API_KEY 

    suspend fun obtenerRespuestaGemini(promptPregunta: String): AiAnswer {
        // 1. Armamos el Request (lo que le vamos a mandar a Gemini)
        val requestBody = AiRequestDto(
            contents = listOf(
                RequestContentDto(
                    parts = listOf(RequestPartDto(text = promptPregunta))
                )
            )
        )

        // Reintento automático en caso de 429 (límite de frecuencia) o 503 (servidor ocupado)
        var intentos = 0
        val maxIntentos = 2
        while (true) {
            try {
                val rawAnswer = RetrofitClient.instanceGemini.generateContent(
                    apiKey = API_KEY,
                    request = requestBody
                )
                return rawAnswer.toDomain()
            } catch (e: retrofit2.HttpException) {
                intentos++
                if ((e.code() == 429 || e.code() == 503) && intentos < maxIntentos) {
                    delay(3000) // Esperar 3 segundos antes de reintentar
                } else {
                    throw e
                }
            }
        }
    }
}
