package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import java.time.DayOfWeek
import java.time.LocalTime

class Pref_Horario(
    var dia: DayOfWeek,
    private var horaInicio: LocalTime,
    private var horaFin: LocalTime,
    var prioridad: Int
){
    
}