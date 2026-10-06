package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces.IOrganizador
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces.IParticipante

class Usuario(
    private val id: Int,
    var nickname: String,
    var email: String
) : IParticipante, IOrganizador {

    var preferencias: PreferenciasUsuario = PreferenciasUsuario(mutableListOf())

    override fun notificar(mensaje: String) {
        // Implementación
    }

    override fun crearGrupo(nombre: String) {
        // Implementación
    }

    override fun invitarMiembro(user: Usuario) {
        // Implementación
    }

    fun crearActividad() {
        // Implementación
    }

    fun getDisponibilidad(): Boolean {
        // Implementación
        return true
    }
}
