package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AiApi {
    // Ya incluimos v1beta en el BASE_URL de RetrofitClient
    @POST("models/{model}")
    suspend fun generateContent(
        // "encoded = true" para evitar que Retrofit rompa los dos puntos ":"
        @Path(value = "model", encoded = true) model: String = "gemini-3.5-flash-lite:generateContent",
        @Query("key") apiKey: String,
        // Usamos el DTO de Request que acabamos de crear para ENVIAR la pregunta
        @Body request: AiRequestDto
    ): AiAnswerDto // Y esperamos recibir la estructura completa (AiAnswerDto)
}
