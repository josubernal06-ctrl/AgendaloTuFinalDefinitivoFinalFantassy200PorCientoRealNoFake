package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

abstract class AbstractActividad(
    open val id: Int,
    open var titulo: String,
    open var descripcion: String,
    open var propietario: Usuario,
    open var esPrivada: Boolean,
    open var segmentoTiempo: SegmentoTiempo
)
