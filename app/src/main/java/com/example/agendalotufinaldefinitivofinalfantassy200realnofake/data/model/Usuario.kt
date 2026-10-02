package com.example.isnotempty.data.model

import java.time.LocalDateTime

data class Usuario(val id: Int, val nombre: String, val email: String) {

    fun crearActividad(idActividad: Int, nombreActividad: String,
                       grupo: Grupo? = null): AbstractActividad {

        return if (grupo != null) {
            ActividadConjunta(
                id = idActividad,
                titulo = nombreActividad,
                descripcion = "",
                propietario = this,
                segmentoTiempo = SegmentoTiempo(
                    inicio = LocalDateTime.now(),
                    final = LocalDateTime.now(),
                    esConjunta = true,
                    puntuacion = 0.0
                ),
                grupo = grupo
            )
        } else {
            ActividadSolitaria(
                id = idActividad,
                titulo = nombreActividad,
                descripcion = "",

                propietario = this,
                segmentoTiempo = SegmentoTiempo(
                    inicio = LocalDateTime.now(),
                    final = LocalDateTime.now(),
                    esConjunta = false,
                    puntuacion = 0.0
                )
            )
        }
    }

    fun getDisponibilidad(): Boolean{
        return true
        //aca seria conectarlo a un boton pa q depende a eso se vea si esta libre o nao
    }
}