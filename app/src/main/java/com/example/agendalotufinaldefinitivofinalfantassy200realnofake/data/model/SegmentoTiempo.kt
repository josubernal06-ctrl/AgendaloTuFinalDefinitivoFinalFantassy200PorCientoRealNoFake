package com.example.isnotempty.data.model

import java.time.LocalDateTime

data class SegmentoTiempo(val inicio: LocalDateTime, val final: LocalDateTime,
                          val esConjunta:Boolean, val puntuacion: Double) {

}
