package com.example.isnotempty.data.repository

import com.example.isnotempty.data.model.Event

/**
 * CAPA DE DATOS (REPOSITORIOS):
 * En esta carpeta (data/repository/) van las clases encargadas de GUARDAR, CARGAR
 * y SINCRONIZAR la información (ya sea en una base de datos local como Room/SQLite,
 * en la memoria de la app o conectándote a servicios en la nube como Google Calendar API o Teams).
 */
class EventRepository {
    private val listaEventos = mutableListOf<Event>()

    fun obtenerEventos(): List<Event> = listaEventos

    fun agregarEvento(evento: Event) {
        listaEventos.add(evento)
    }
}
