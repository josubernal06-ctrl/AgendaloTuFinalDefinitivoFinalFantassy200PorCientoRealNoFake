package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository

sealed class Resultado<out T> {
    data class Exito<out T>(val data: T) : Resultado<T>()
    data class Error(val excepcion: Throwable) : Resultado<Nothing>()
    object Cargando : Resultado<Nothing>()
}
