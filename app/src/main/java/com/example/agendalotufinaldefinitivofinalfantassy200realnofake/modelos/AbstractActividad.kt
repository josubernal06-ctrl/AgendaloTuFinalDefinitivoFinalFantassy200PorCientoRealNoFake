package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

abstract class AbstractActividad(
    private val id: Int,
    var titulo: String,
    private var descripcion: String,
    var propietario: Usuario,
    private var esPrivada: Boolean,
    private var segmentoTiempo: SegmentoTiempo
)
