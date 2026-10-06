package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class Grupo(
    private val id: Int,
    var nombreGrupo: String,
    private var propietario: Usuario,
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
