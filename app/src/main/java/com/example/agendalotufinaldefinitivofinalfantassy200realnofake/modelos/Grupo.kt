package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class Grupo(
    val id: Int,
    var nombreGrupo: String,
    var propietario: Usuario,
    var descripcion: String = "",
    var codigoInvitacion: String = "",
    var miebros: List<Usuario> = emptyList()
) {
    fun agregarMiembro(user: Usuario) {
        if (miebros is MutableList) {
            (miebros as MutableList).add(user)
        }
    }

    fun eleiminarMiembro(user: Usuario) {
        if (miebros is MutableList) {
            (miebros as MutableList).remove(user)
        }
    }
}
