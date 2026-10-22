package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadEntity
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
    // Usamos reflection para acceder a los campos privados requeridos por el diagrama UML estricto
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

    return ActividadEntity(
        id = realId,
        titulo = this.titulo,
        descripcion = desc,
        esPrivada = priv,
        propietarioId = 1,
        propietarioNickname = this.propietario.nickname,
        inicio = inicio.toString(),
        fin = fin.toString(),
        esConjunta = this is ActividadConjunta,
        puntuacion = seg.puntuacion
    )
}
