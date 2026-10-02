package com.example.isnotempty.ui.theme

import androidx.compose.ui.graphics.Color

// Data Class que agrupa los 4 colores de cada tema
data class TemaColor(
    val id: Int,
    val fondo: Color,
    val botones: Color,
    val detalles: Color,
    val letras: Color
)

// Filtro de luz cálida
const val calido: Float = 0.15f

fun Color.toWarm(warmth: Float = calido): Color {
    val r = red
    val g = green * (1f - warmth * 0.05f)
    val b = blue * (1f - warmth * 0.45f)
    return Color(red = r, green = g, blue = b, alpha = alpha)
}

fun obtenerTema(numero: Int): TemaColor {
    val temaBase = when (numero) {
        1 -> TemaColor(1, tema1_fondo, tema1_botones, tema1_detalles, tema1_letras)
        2 -> TemaColor(2, tema2_fondo, tema2_botones, tema2_detalles, tema2_letras)
        3 -> TemaColor(3, tema3_fondo, tema3_botones, tema3_detalles, tema3_letras)
        4 -> TemaColor(4, tema4_fondo, tema4_botones, tema4_detalles, tema4_letras)
        5 -> TemaColor(5, tema5_fondo, tema5_botones, tema5_secundario, tema5_letras)
        6 -> TemaColor(6, tema6_fondo, tema6_botones, tema6_detalles, tema6_letras)
        7 -> TemaColor(7, tema7_fondo, tema7_botones, tema7_secundario, tema7_letras)
        8 -> TemaColor(8, tema8_fondo, tema8_botones, tema8_detalles, tema8_letras)
        9 -> TemaColor(9, tema9_fondo, tema9_botones, tema9_detalles, tema9_letras)
        10 -> TemaColor(10, tema10_fondo, tema10_botones, tema10_detalles, tema10_letras)
        11 -> TemaColor(11, tema11_fondo, tema11_botones, tema11_detalles, tema11_letras)
        else -> TemaColor(1, tema1_fondo, tema1_botones, tema1_detalles, tema1_letras)
    }

    val nivelCalido = when (numero) {
        3 -> 0.15f
        10 -> 0.30f
        else -> calido
    }

    return TemaColor(
        id = temaBase.id,
        fondo = temaBase.fondo.toWarm(nivelCalido),
        botones = temaBase.botones.toWarm(nivelCalido),
        detalles = temaBase.detalles.toWarm(nivelCalido),
        letras = temaBase.letras.toWarm(nivelCalido)
    )
}
