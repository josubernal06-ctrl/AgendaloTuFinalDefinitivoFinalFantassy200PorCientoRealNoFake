package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class Grupo(
    private val id: Int,
    var nombreGrupo: String,
    private var propietario: Usuario,
    var miembros: MutableList<Usuario> = mutableListOf()
) {
    fun agregarMiembro(user: Usuario) {
        // TODO: Añadir el objeto user a la lista miembros
    }

    fun eliminarMiembro(user: Usuario) {
        // TODO: Buscar y remover el objeto user de la lista miembros
    }
}