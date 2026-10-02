package com.example.isnotempty.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * CAPA DE COMPONENTES REUTILIZABLES:
 * En esta carpeta (ui/components/) van componentes visuales pequeños y genéricos
 * que se usan en múltiples pantallas de la app (Botones personalizados, Tarjetas,
 * Modales de confirmación, Selectores de hora, Chips de etiquetas).
 */
@Composable
fun BotonPersonalizado(
    texto: String,
    onClick: () -> Unit,
    colorFondo: Color,
    colorTexto: Color
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = colorFondo)
    ) {
        Text(text = texto, color = colorTexto)
    }
}
