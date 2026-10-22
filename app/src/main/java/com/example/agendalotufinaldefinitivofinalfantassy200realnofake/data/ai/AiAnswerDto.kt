package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai
import com.google.gson.annotations.SerializedName

// 1. La clase principal que recibe Retrofit
data class AiAnswerDto(
    @SerializedName("candidates") val candidates: List<CandidateDto>? = null
)

// 2. La lista de candidatos (usualmente solo viene uno)
data class CandidateDto(
    @SerializedName("content") val content: ContentDto? = null
)

// 3. El contenido del mensaje
data class ContentDto(
    @SerializedName("parts") val parts: List<PartDto>? = null,
    @SerializedName("role") val role: String? = null // Opcional, dirá "model"
)

// 4. La parte final que contiene el texto puro
data class PartDto(
    @SerializedName("text") val text: String? = null
)