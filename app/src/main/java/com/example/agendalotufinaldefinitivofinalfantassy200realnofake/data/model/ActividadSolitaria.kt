package com.example.isnotempty.data.model

class ActividadSolitaria(override val id: Int,
                         override val titulo: String,
                         override val descripcion: String,
                         override val propietario: Usuario,
                         override val esPrivada: Boolean = false,
                         override val segmentoTiempo: SegmentoTiempo): AbstractActividad {
}