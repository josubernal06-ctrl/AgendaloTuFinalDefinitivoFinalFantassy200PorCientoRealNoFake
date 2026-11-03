package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadEntity
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
    val propietario = Usuario(
        id = this.propietarioId,
        nickname = this.propietarioNickname,
        email = ""
    )

    return if (this.esConjunta) {
        ActividadConjunta(
            id = this.id,
            titulo = this.titulo,
            descripcion = this.descripcion,
            propietario = propietario,
            esPrivada = this.esPrivada,
            segmentoTiempo = segmento
        )
    } else {
        ActividadSolitaria(
            id = this.id,
            titulo = this.titulo,
            descripcion = this.descripcion,
            propietario = propietario,
            esPrivada = this.esPrivada,
            segmentoTiempo = segmento
        )
    }
}

fun AbstractActividad.toEntity(userId: String = ""): ActividadEntity {
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
    val propietario = Usuario(id = 0, nickname = "Propietario", email = this.propietarioId)
    val code = if (this.codigoInvitacion.isNotBlank()) this.codigoInvitacion else "GRP${if (this.id > 0) this.id else 101}920"
    return Grupo(
        id = this.id,
        nombreGrupo = this.nombreGrupo,
        descripcion = this.descripcion,
        propietario = propietario,
        codigoInvitacion = code
    )
}

fun Grupo.toEntity(): GrupoEntity {
    return GrupoEntity(
        id = this.id,
        nombreGrupo = this.nombreGrupo,
        descripcion = this.descripcion,
        propietarioId = this.propietario.email.ifEmpty { this.propietario.id.toString() },
        codigoInvitacion = this.codigoInvitacion
    )
}
