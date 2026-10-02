package com.example.isnotempty.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.isnotempty.ui.theme.TemaColor

/**
 * Tarjeta interactiva con contador y botones extraída a la capa de componentes.
 */
@Composable
fun ContadorCard(
    contador: Int,
    onSumarClick: () -> Unit,
    onReiniciarClick: () -> Unit,
    tema: TemaColor,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Contador de clics: $contador",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = tema.letras
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onSumarClick,
                    colors = ButtonDefaults.buttonColors(containerColor = tema.botones)
                ) {
                    Text(text = "Sumar (+1)", color = tema.fondo)
                }

                Button(
                    onClick = onReiniciarClick,
                    colors = ButtonDefaults.buttonColors(containerColor = tema.letras)
                ) {
                    Text(text = "Reiniciar", color = tema.fondo)
                }
            }
        }
    }
}

/**
 * Campo de entrada de texto reutilizable adaptado a las fuentes y colores del tema.
 */
@Composable
fun EntradaTextoField(
    valor: String,
    onValueChange: (String) -> Unit,
    label: String,
    tema: TemaColor,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(label, color = tema.letras) },
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = tema.letras,   // Color del texto escrito al enfocar
            unfocusedTextColor = tema.letras, // Color del texto escrito al desenfocar
            focusedBorderColor = tema.botones,
            unfocusedBorderColor = tema.letras,
            focusedLabelColor = tema.botones,
            unfocusedLabelColor = tema.botones //antes letrascolor
        )
    )
}
