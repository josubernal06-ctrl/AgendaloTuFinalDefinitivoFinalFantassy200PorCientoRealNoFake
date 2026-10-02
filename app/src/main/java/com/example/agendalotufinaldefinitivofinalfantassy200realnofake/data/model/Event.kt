package com.example.isnotempty.data.model

import java.time.LocalDateTime

/**
 * CAPA DE DATOS (MODELOS DE DATOS):
 * En esta carpeta (data/model/) deben ir todas las Data Classes que representan
 * la información de tu aplicación (Eventos del calendario, Tareas, Recordatorios, Usuarios, etc.).
 */
data class Event(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val fechaInicio: LocalDateTime,
    val fechaFin: LocalDateTime,
    val colorTemaId: Int = 1
)
