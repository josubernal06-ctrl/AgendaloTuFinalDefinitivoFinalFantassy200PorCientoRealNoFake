package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

abstract class AbstractActividad(
    val id: Int,
    var titulo: String,
    var descripcion: String,
    var propietario: Usuario,
    var esPrivada: Boolean,
    var segmentoTiempo: SegmentoTiempo
)
