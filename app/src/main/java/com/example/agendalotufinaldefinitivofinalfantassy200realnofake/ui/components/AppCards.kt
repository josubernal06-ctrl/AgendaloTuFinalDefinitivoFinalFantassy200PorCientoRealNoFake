package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

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
    TextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(label, color = tema.letras) },
        /*placeholder = { Text(label, color = tema.letras.copy(alpha = 0.6f)) },*/
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = tema.fondo,
            unfocusedContainerColor = tema.fondo,
            focusedTextColor = tema.letras,
            unfocusedTextColor = tema.letras,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = tema.letras,
            unfocusedLabelColor = tema.letras.copy(alpha = 0.7f),
            cursorColor = tema.letras
        )
    )
}

/**
 * Tarjeta para mostrar un horario o clase agendada.
 */
@Composable
fun HorarioCard(
    titulo: String,
    descripcion: String,
    hora: String,
    tema: TemaColor,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f))
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar del usuario",
                modifier = Modifier.size(80.dp),
                tint = tema.letras
            )
             Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = titulo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = tema.letras
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion,
                    fontSize = 14.sp,
                    color = tema.letras.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hora,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = tema.botones
                )
            }
        }

    }
}

/**
 * Tarjeta para mostrar un feriado público.
 */
@Composable
fun FeriadoCard(
    nombre: String,
    fecha: String,
    tema: TemaColor,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = tema.letras
            )
            if (fecha.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Fecha: $fecha",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = tema.botones
                )
            }
        }
    }
}
