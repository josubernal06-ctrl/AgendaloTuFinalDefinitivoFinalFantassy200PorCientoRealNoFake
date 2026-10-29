package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.mock

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiAnswer
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository.IAiRepository
import kotlinx.coroutines.delay

class MockAiRepository : IAiRepository {
    override suspend fun obtenerRespuestaGemini(promptPregunta: String): AiAnswer {
        delay(1000)
        return AiAnswer("Mock recommendation: You should schedule your group activity at 15:00 tomorrow, when everyone is free.")
    }
}
