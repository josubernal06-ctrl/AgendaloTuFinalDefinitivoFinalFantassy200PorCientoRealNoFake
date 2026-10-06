package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces.IOrganizador
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces.IParticipante

class Usuario(
    private val id: Int,
    var nickname: String,
    var email: String,
    var preferencias: PreferenciasUsuario
) : IParticipante, IOrganizador {

    override fun notificar(mensaje: String) {
        // TODO: Implementar lógica para enviar notificación al usuario
    }

    override fun crearGrupo(nombre: String) {
        // TODO: Implementar inicialización de un nuevo Grupo
    }

    override fun invitarMiembro(user: Usuario) {
        // TODO: Implementar lógica para invitar a otro usuario a un grupo
    }

    fun crearActividad() {
        // TODO: Implementar creación de ActividadSolitaria o ActividadConjunta
    }

    fun getDisponibilidad() {
        // TODO: Calcular y retornar la disponibilidad en base a PreferenciasUsuario y Segmentos ocupados
    }
}