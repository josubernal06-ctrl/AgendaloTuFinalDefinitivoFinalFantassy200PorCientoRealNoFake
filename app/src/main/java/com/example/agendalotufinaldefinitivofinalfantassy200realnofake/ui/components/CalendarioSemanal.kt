package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@Composable
fun CalendarioSemanal(
    tema: TemaColor,
    modifier: Modifier = Modifier
) {
    // Datos de maquetación de la semana
    val diasSemana = listOf(
        Pair("Lun", "14"),
        Pair("Mar", "15"),
        Pair("Mié", "16"),
        Pair("Jue", "17"),
        Pair("Vie", "18"),
        Pair("Sáb", "19"),
        Pair("Dom", "20")
    )

    // Estado simple de selección visual (por defecto el día de ejemplo Mié 16)
    var diaSeleccionadoIndex by remember { mutableIntStateOf(2) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Cabecera estática con Nombre del Mes y botones de navegación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Octubre 2026",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = tema.letras
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { /* Maqueta: Acción para semana anterior */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Semana anterior",
                            tint = tema.letras
                        )
                    }

                    IconButton(
                        onClick = { /* Maqueta: Acción para semana siguiente */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Semana siguiente",
                            tint = tema.letras
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fila de los 7 días de la semana (maqueta visual)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                diasSemana.forEachIndexed { index, dia ->
                    val esSeleccionado = index == diaSeleccionadoIndex
                    val esHoy = index == 2 // Indicador de hoy para el día de ejemplo (Mié 16)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                color = if (esSeleccionado) tema.botones else tema.fondo.copy(alpha = 0.6f)
                            )
                            .clickable {
                                diaSeleccionadoIndex = index
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = dia.first,
                            fontSize = 11.sp,
                            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium,
                            color = if (esSeleccionado) tema.fondo else tema.letras.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = dia.second,
                            fontSize = 14.sp,
                            fontWeight = if (esSeleccionado || esHoy) FontWeight.Bold else FontWeight.Normal,
                            color = if (esSeleccionado) tema.fondo else tema.letras,
                            textAlign = TextAlign.Center
                        )

                        // Indicador visual de día de hoy
                        if (esHoy && !esSeleccionado) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(tema.botones)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }
}
