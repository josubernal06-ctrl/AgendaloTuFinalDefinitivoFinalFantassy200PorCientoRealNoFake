package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai

fun AiAnswerDto.toDomain(): AiAnswer {
    // Gemini devuelve una lista de "candidates", agarramos el primero.
    // Dentro tiene "content", y dentro "parts", agarramos la primera parte y sacamos el texto.
    // Si algo falla o viene nulo, devolvemos un texto de fallback.
    val textoRespuesta = this.candidates?.firstOrNull()
        ?.content?.parts?.firstOrNull()
        ?.text ?: "No pude procesar la respuesta."

    return AiAnswer(answer = textoRespuesta)
}