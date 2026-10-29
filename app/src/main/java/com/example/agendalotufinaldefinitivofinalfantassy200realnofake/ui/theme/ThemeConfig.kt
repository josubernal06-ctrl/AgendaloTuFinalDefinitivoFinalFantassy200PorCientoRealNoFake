package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme

import androidx.compose.ui.graphics.Color

data class TemaColor(
    val numero: Int,
    val fondo: Color,
    val botones: Color,
    val detalles: Color,
    val letras: Color
)

fun Color.toWarm(factor: Float = 0.15f): Color {
    val warmRed = 0.85f
    val warmGreen = 0.80f
    val warmBlue = 0.70f

    val newRed = this.red * (1 - factor) + warmRed * factor
    val newGreen = this.green * (1 - factor) + warmGreen * factor
    val newBlue = this.blue * (1 - factor) + warmBlue * factor

    return Color(newRed, newGreen, newBlue, this.alpha)
}

fun obtenerTema(numero: Int, calido: Float = 0.0f): TemaColor {
    val temaBase = when (numero) {
        1 -> TemaColor(1, tema1_fondo, tema1_botones, tema1_detalles, tema1_letras)
        2 -> TemaColor(2, tema2_fondo, tema2_botones, tema2_detalles, tema2_letras)
        3 -> TemaColor(3, tema3_fondo, tema3_botones, tema3_detalles, tema3_letras)
        4 -> TemaColor(4, tema4_fondo, tema4_botones, tema4_detalles, tema4_letras)
        5 -> TemaColor(5, tema5_fondo, tema5_botones, tema5_detalles, tema5_letras)
        6 -> TemaColor(6, tema6_fondo, tema6_botones, tema6_detalles, tema6_letras)
        7 -> TemaColor(7, tema7_fondo, tema7_botones, tema7_detalles, tema7_letras)
        8 -> TemaColor(8, tema8_fondo, tema8_botones, tema8_detalles, tema8_letras)
        9 -> TemaColor(9, tema9_fondo, tema9_botones, tema9_detalles, tema9_letras)
        10 -> TemaColor(10, tema10_fondo, tema10_botones, tema10_detalles, tema10_letras)
        11 -> TemaColor(11, tema11_fondo, tema11_botones, tema11_detalles, tema11_letras)
        12 -> TemaColor(12, tema12_fondo, tema12_botones, tema12_detalles, tema12_letras)
        13 -> TemaColor(13, tema13_fondo, tema13_botones, tema13_detalles, tema13_letras)
        else -> TemaColor(13, tema13_fondo, tema13_botones, tema13_detalles, tema13_letras)
    }

    val nivelCalido = when (numero) {
        3 -> 0.15f
        10 -> 0.30f
        12, 13 -> 0.0f
        else -> calido
    }

    return TemaColor(
        numero = temaBase.numero,
        fondo = temaBase.fondo.toWarm(nivelCalido),
        botones = temaBase.botones.toWarm(nivelCalido),
        detalles = temaBase.detalles.toWarm(nivelCalido),
        letras = temaBase.letras
    )
}
