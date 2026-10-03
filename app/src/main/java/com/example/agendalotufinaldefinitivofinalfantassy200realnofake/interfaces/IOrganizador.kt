package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario

interface IOrganizador {
    fun crearGrupo(nombre: String)
    fun invitarMiembro(user: Usuario)
}