package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class ActividadSolitaria(
    id: Int,
    titulo: String,
    descripcion: String,
    propietario: Usuario,
    esPrivada: Boolean,
    segmentoTiempo: SegmentoTiempo
) : AbstractActividad(id, titulo, descripcion, propietario, esPrivada, segmentoTiempo)