package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.model

interface AbstractActividad{
    val id:Int
    val titulo:String
    val descripcion:String
    val propietario: Usuario
    val esPrivada: Boolean
    val segmentoTiempo: SegmentoTiempo
}