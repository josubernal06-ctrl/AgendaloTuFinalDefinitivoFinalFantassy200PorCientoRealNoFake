package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.actividad.ActividadEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import java.time.LocalDateTime

fun ActividadEntity.toDomain(): AbstractActividad {
    val segmento = SegmentoTiempo(
        inicio = LocalDateTime.parse(this.inicio),
        fin = LocalDateTime.parse(this.fin),
        esConjunta = this.esConjunta,
        puntuacion = this.puntuacion
    )
    val propietario = Usuario(id = this.propietarioId, nickname = this.propietarioNickname, email = "")

    return if (this.esConjunta) {
        ActividadConjunta(this.id, this.titulo, this.descripcion, propietario, this.esPrivada, segmento)
    } else {
        ActividadSolitaria(this.id, this.titulo, this.descripcion, propietario, this.esPrivada, segmento)
    }
}

fun AbstractActividad.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = this.id,
        titulo = this.titulo,
        descripcion = this.descripcion,
        esPrivada = this.esPrivada,
        propietarioId = this.propietario.id,
        propietarioNickname = this.propietario.nickname,
        inicio = this.segmentoTiempo.inicio.toString(),
        fin = this.segmentoTiempo.fin.toString(),
        esConjunta = this is ActividadConjunta,
        puntuacion = this.segmentoTiempo.puntuacion
    )
}

fun GrupoEntity.toDomain(): Grupo {
    val propietario = Usuario(id = this.propietarioId, nickname = this.propietarioNickname, email = this.propietarioEmail)
    return Grupo(id = this.id, nombreGrupo = this.nombreGrupo, propietario = propietario)
}

fun Grupo.toEntity(): GrupoEntity {
    return GrupoEntity(
        id = this.id,
        nombreGrupo = this.nombreGrupo,
        propietarioId = this.propietario.id,
        propietarioNickname = this.propietario.nickname,
        propietarioEmail = this.propietario.email
    )
}
