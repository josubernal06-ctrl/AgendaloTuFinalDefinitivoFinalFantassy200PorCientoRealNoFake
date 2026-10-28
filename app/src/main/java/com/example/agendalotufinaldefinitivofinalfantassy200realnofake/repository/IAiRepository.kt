package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiAnswer

interface IAiRepository {
    suspend fun obtenerRespuestaGemini(promptPregunta: String): AiAnswer
}
