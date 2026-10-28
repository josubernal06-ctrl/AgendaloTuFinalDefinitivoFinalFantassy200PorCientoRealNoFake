package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class ActividadConjunta(
    id: Int,
    titulo: String,
    descripcion: String,
    propietario: Usuario,
    esPrivada: Boolean,
    segmentoTiempo: SegmentoTiempo,
    val grupoId: Int = -1,
    var participantes: List<Usuario> = emptyList()
) : AbstractActividad(id, titulo, descripcion, propietario, esPrivada, segmentoTiempo)
