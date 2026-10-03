package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import java.time.LocalDateTime

class SegmentoTiempo(
    private var inicio: LocalDateTime,
    private var fin: LocalDateTime,
    var esConjunta: Boolean,
    var puntuacion: Double
)