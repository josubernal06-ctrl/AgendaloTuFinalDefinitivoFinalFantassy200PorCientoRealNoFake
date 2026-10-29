package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Holiday
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarGridView(
    currentYearMonth: YearMonth,
    onYearMonthChange: (YearMonth) -> Unit,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    actividades: List<AbstractActividad>,
    holidays: List<Holiday>,
    tema: TemaColor,
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf("DOM", "LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB")

    val firstDayOfMonth = currentYearMonth.atDay(1)
    val emptyLeadingCells = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonthCount = currentYearMonth.lengthOfMonth()
    val today = LocalDate.now()

    val monthTitle = currentYearMonth.format(
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))
    ).uppercase(Locale("es", "ES"))

    val totalCells = emptyLeadingCells + daysInMonthCount
    val rowsCount = kotlin.math.ceil(totalCells / 7.0).toInt()
    val gridHeight = (rowsCount * 54 + (rowsCount - 1) * 6).dp

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Encabezado con flechas de navegación y título del Mes/Año
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onYearMonthChange(currentYearMonth.minusMonths(1)) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Mes anterior",
                    tint = tema.letras
                )
            }

            Text(
                text = monthTitle,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = tema.letras
            )

            IconButton(onClick = { onYearMonthChange(currentYearMonth.plusMonths(1)) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Mes siguiente",
                    tint = tema.letras
                )
            }
        }

        // Cabecera con los días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = tema.letras.copy(alpha = 0.8f),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cuadrícula con los días del mes dinámicos
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(gridHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            userScrollEnabled = false
        ) {
            items(count = emptyLeadingCells) {
                Box(modifier = Modifier.fillMaxSize())
            }

            items(count = daysInMonthCount) { index ->
                val dayNumber = index + 1
                val dayDate = currentYearMonth.atDay(dayNumber)
                val isSelected = dayDate == selectedDate
                val isToday = dayDate == today

                // Verificar si hay feriado en esta fecha
                val hasHoliday = holidays.any { feriado ->
                    try {
                        LocalDate.parse(feriado.date) == dayDate
                    } catch (_: Exception) {
                        false
                    }
                }

                // Verificar si hay actividad del usuario en esta fecha
                val hasUserActivity = actividades.any { act ->
                    try {
                        act.segmentoTiempo.inicio.toLocalDate() == dayDate
                    } catch (_: Exception) {
                        false
                    }
                }

                val cardShape = RoundedCornerShape(8.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(cardShape)
                        .background(
                            color = when {
                                isSelected -> tema.botones.copy(alpha = 0.2f)
                                isToday -> tema.detalles.copy(alpha = 0.25f)
                                else -> tema.detalles.copy(alpha = 0.08f)
                            }
                        )
                        .then(
                            if (isSelected) {
                                Modifier.border(2.dp, tema.botones, cardShape)
                            } else {
                                Modifier
                            }
                        )
                        .clickable { onDateSelected(dayDate) }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = dayNumber.toString(),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = if (isSelected) tema.botones else tema.letras
                        )

                        // Indicadores visuales debajo de la fecha
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 2.dp)
                        ) {
                            // Línea Verde = Feriado
                            if (hasHoliday) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFF22C55E))
                                )
                            }

                            // Línea Azul = Actividad del usuario
                            if (hasUserActivity) {
                                Box(
                                    modifier = Modifier
                                        .width(16.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(Color(0xFF3B82F6))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
