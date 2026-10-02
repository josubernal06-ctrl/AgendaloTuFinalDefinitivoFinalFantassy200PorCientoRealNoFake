package com.example.isnotempty.data.model

data class Grupo(val id: Int, val nombreGrupo: String,val propietario: Usuario,val integrantes: List<Usuario>){
    fun agregarMiembro(nuevoMiembro: Usuario): Grupo {
        return this.copy(integrantes= this.integrantes.plus(nuevoMiembro))
    }
    fun eliminarMiembro(miembroAEliminar: Usuario): Grupo {
        return this.copy(integrantes = this.integrantes.minus(miembroAEliminar))
    }
}