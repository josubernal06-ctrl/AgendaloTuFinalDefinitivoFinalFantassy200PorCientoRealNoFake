package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai
import com.google.gson.annotations.SerializedName

// Estructura para enviar (Request) a Gemini
data class AiRequestDto(
    @SerializedName("contents") val contents: List<RequestContentDto>
)

data class RequestContentDto(
    @SerializedName("parts") val parts: List<RequestPartDto>
)

data class RequestPartDto(
    @SerializedName("text") val text: String
)