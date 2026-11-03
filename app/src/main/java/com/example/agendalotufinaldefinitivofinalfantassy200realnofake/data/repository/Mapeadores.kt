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
    val propietario = Usuario(id = this.propietarioId, nickname = this.propietarioNickname, email = "")

    return if (this.esConjunta) {
        ActividadConjunta(
            id = this.id,
            titulo = this.titulo,
            descripcion = this.descripcion,
            propietario = propietario,
            esPrivada = this.esPrivada,
            segmentoTiempo = segmento,
            grupoId = this.grupoId ?: -1
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

fun AbstractActividad.toEntity(): ActividadEntity {
    val clazz = AbstractActividad::class.java

    val idField = clazz.getDeclaredField("id")
    idField.isAccessible = true
    val realId = idField.get(this) as Int
    
    val descField = clazz.getDeclaredField("descripcion")
    descField.isAccessible = true
    val desc = descField.get(this) as String
    
    val privField = clazz.getDeclaredField("esPrivada")
    privField.isAccessible = true
    val priv = privField.get(this) as Boolean
    
    val segField = clazz.getDeclaredField("segmentoTiempo")
    segField.isAccessible = true
    val seg = segField.get(this) as SegmentoTiempo

    val segClazz = SegmentoTiempo::class.java
    val inicioField = segClazz.getDeclaredField("inicio")
    inicioField.isAccessible = true
    val inicio = inicioField.get(seg) as LocalDateTime
    
    val finField = segClazz.getDeclaredField("fin")
    finField.isAccessible = true
    val fin = finField.get(seg) as LocalDateTime

    val isConjunta = this is ActividadConjunta && this.grupoId > 0
    val gId = if (this is ActividadConjunta) this.grupoId.takeIf { it > 0 } else null

    return ActividadEntity(
        id = realId,
        titulo = this.titulo,
        descripcion = desc,
        esPrivada = priv,
        propietarioId = this.propietario.id,
        propietarioNickname = this.propietario.nickname,
        inicio = inicio.toString(),
        fin = fin.toString(),
        esConjunta = isConjunta,
        puntuacion = seg.puntuacion,
        grupoId = gId
    )
}

fun GrupoEntity.toDomain(): Grupo {
    val propietario = Usuario(id = this.propietarioId.hashCode(), nickname = "Propietario", email = this.propietarioId)
    return Grupo(
        id = this.id,
        nombreGrupo = this.nombreGrupo,
        descripcion = this.descripcion,
        propietario = propietario,
        codigoInvitacion = this.codigoInvitacion
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
