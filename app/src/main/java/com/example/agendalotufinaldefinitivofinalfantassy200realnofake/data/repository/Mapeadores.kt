package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import java.time.LocalDateTime

fun ActividadEntity.toDomain(): AbstractActividad {
    val segmento = SegmentoTiempo(
        inicio = LocalDateTime.parse(this.fechaInicio),
        fin = LocalDateTime.parse(this.fechaFin),
        esConjunta = this.esConjunta,
        puntuacion = 1.0
    )
    val propietario = Usuario(
        id = this.userId.hashCode(),
        nickname = this.userId.substringBefore("@").ifEmpty { "Usuario" },
        email = this.userId
    )

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

fun AbstractActividad.toEntity(userId: String): ActividadEntity {
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

    val isConjunta = this is ActividadConjunta
    val gId = if (isConjunta) (this as ActividadConjunta).grupoId.takeIf { it > 0 } else null

    return ActividadEntity(
        id = realId,
        titulo = this.titulo,
        descripcion = desc,
        esPrivada = priv,
        userId = userId,
        grupoId = gId,
        esConjunta = isConjunta,
        fechaInicio = inicio.toString(),
        fechaFin = fin.toString()
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
