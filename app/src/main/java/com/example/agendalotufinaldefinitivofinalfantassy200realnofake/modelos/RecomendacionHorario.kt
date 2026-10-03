package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class RecomendacionHorario(
    var mejorSlot: SegmentoTiempo,
    var opcionesAlternativas: List<SegmentoTiempo>,
    var conflictos: List<Conflicto>
)