package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.clima

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Clima
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.HoraClima

fun ClimaDto.toDomain(): Clima {
    val pronostico = this.hourly.time.mapIndexed { index, timeStr ->
        val fechaStr = if (timeStr.contains("T")) timeStr.substringBefore("T") else ""
        val horaFormateada = if (timeStr.contains("T")) timeStr.substringAfter("T") else timeStr
        HoraClima(
            fechaISO = fechaStr,
            hora = horaFormateada,
            temperatura = this.hourly.temperature2m.getOrElse(index) { 0.0 },
            probabilidadLluvia = this.hourly.precipitationProbability.getOrElse(index) { 0 },
            precipitacion = this.hourly.precipitation.getOrElse(index) { 0.0 }
        )
    }
    return Clima(
        zonaHoraria = this.timezone,
        pronosticoPorHora = pronostico
    )
}
