package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

data class HoraClima(
    val hora: String,
    val temperatura: Double,
    val probabilidadLluvia: Int,
    val precipitacion: Double
)

data class Clima(
    val zonaHoraria: String,
    val pronosticoPorHora: List<HoraClima>
)
