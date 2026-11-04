package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActivityScreen(
    tema: TemaColor,
    usuarioActual: Usuario,
    onBackClick: () -> Unit,
    onSaveClick: (AbstractActividad) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.of(10, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(12, 0)) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val textColor = tema.letras
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()) }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Actividad", color = tema.fondo, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = tema.fondo)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = tema.botones)
            )
        },
        containerColor = tema.fondo
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título de la actividad *", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    focusedBorderColor = tema.botones,
                    unfocusedBorderColor = textColor.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (Opcional)", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    focusedBorderColor = tema.botones,
                    unfocusedBorderColor = textColor.copy(alpha = 0.5f)
                )
            )

            Text("Fecha y Horarios", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)

            // Selector de Fecha
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Seleccionar fecha",
                        tint = tema.botones,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text("Fecha", fontSize = 12.sp, color = textColor.copy(alpha = 0.7f))
                        Text(
                            text = selectedDate.format(dateFormatter).replaceFirstChar { it.uppercase() },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    }
                }
            }

            // Selector de Hora Inicio y Hora Fin
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Hora Inicio
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showStartTimePicker = true },
                    colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Hora inicio",
                            tint = tema.botones,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text("Inicio", fontSize = 11.sp, color = textColor.copy(alpha = 0.7f))
                            Text(
                                text = startTime.format(timeFormatter),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }

                // Hora Fin
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showEndTimePicker = true },
                    colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Hora fin",
                            tint = tema.botones,
                            modifier = Modifier.size(22.dp)
                        )
                        Column {
                            Text("Fin", fontSize = 11.sp, color = textColor.copy(alpha = 0.7f))
                            Text(
                                text = endTime.format(timeFormatter),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (titulo.isBlank()) {
                        errorMessage = "El título no puede estar vacío"
                        return@Button
                    }

                    val inicio = LocalDateTime.of(selectedDate, startTime)
                    val fin = LocalDateTime.of(selectedDate, endTime)

                    if (fin.isBefore(inicio) || fin.isEqual(inicio)) {
                        errorMessage = "La hora de fin debe ser posterior a la hora de inicio"
                        return@Button
                    }

                    val segmento = SegmentoTiempo(
                        inicio = inicio,
                        fin = fin,
                        esConjunta = false,
                        puntuacion = 0.0
                    )

                    val actividad = ActividadSolitaria(
                        id = (1000..9999).random(),
                        titulo = titulo,
                        descripcion = descripcion,
                        propietario = usuarioActual,
                        esPrivada = false,
                        segmentoTiempo = segmento
                    )

                    onSaveClick(actividad)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = tema.botones, contentColor = tema.fondo),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Guardar Actividad", color = tema.fondo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }

    // Diálogo de Selección de Fecha
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.of("UTC")).toLocalDate()
                        }
                        showDatePicker = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tema.botones,
                        contentColor = tema.fondo
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Aceptar", color = tema.fondo, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDatePicker = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tema.detalles.copy(alpha = 0.4f),
                        contentColor = textColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cancelar", color = textColor, fontWeight = FontWeight.Bold)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = tema.botones,
                    selectedDayContentColor = tema.fondo,
                    todayDateBorderColor = tema.botones,
                    todayContentColor = tema.botones
                )
            )
        }
    }

    // Diálogo de Selección de Hora Inicio
    if (showStartTimePicker) {
        ThemedTimePickerDialog(
            tema = tema,
            initialTime = startTime,
            title = "Seleccionar Hora de Inicio",
            onDismiss = { showStartTimePicker = false },
            onTimeSelected = { startTime = it }
        )
    }

    // Diálogo de Selección de Hora Fin
    if (showEndTimePicker) {
        ThemedTimePickerDialog(
            tema = tema,
            initialTime = endTime,
            title = "Seleccionar Hora de Fin",
            onDismiss = { showEndTimePicker = false },
            onTimeSelected = { endTime = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedTimePickerDialog(
    tema: TemaColor,
    initialTime: LocalTime,
    title: String,
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute))
                onDismiss()
            }) {
                Text("Aceptar", color = tema.botones, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = tema.letras)
            }
        },
        title = { Text(text = title, color = tema.letras, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = tema.detalles.copy(alpha = 0.2f),
                    clockDialSelectedContentColor = tema.fondo,
                    clockDialUnselectedContentColor = tema.letras,
                    selectorColor = tema.botones,
                    containerColor = tema.fondo,
                    periodSelectorBorderColor = tema.botones,
                    periodSelectorSelectedContainerColor = tema.botones,
                    periodSelectorUnselectedContainerColor = tema.detalles.copy(alpha = 0.1f),
                    periodSelectorSelectedContentColor = tema.fondo,
                    periodSelectorUnselectedContentColor = tema.letras,
                    timeSelectorSelectedContainerColor = tema.botones,
                    timeSelectorUnselectedContainerColor = tema.detalles.copy(alpha = 0.15f),
                    timeSelectorSelectedContentColor = tema.fondo,
                    timeSelectorUnselectedContentColor = tema.letras
                )
            )
        },
        containerColor = tema.fondo
    )
}

@Preview(showBackground = true)
@Composable
fun CreateActivityScreenPreview() {
    val sampleUser = Usuario(id = 1, nickname = "Josue", email = "josue@example.com")
    CreateActivityScreen(
        tema = obtenerTema(1),
        usuarioActual = sampleUser,
        onBackClick = {},
        onSaveClick = {}
    )
}
