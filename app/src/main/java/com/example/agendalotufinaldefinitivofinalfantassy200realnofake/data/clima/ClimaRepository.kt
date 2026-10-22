package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.clima

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Clima

class ClimaRepository {
    suspend fun obtenerClimaActual(): Clima {
        val climaDto = RetrofitClient.instance.obtenerClima()
        return climaDto.toDomain()
    }
}
