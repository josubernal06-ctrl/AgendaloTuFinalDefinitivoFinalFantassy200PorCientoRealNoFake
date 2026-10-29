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
        miebros = miebros + user
    }

    fun eleiminarMiembro(user: Usuario) {
        miebros = miebros - user
    }
}
