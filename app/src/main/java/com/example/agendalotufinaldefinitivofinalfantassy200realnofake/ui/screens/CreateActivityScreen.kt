package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
    var esPrivada by remember { mutableStateOf(false) }
    var esConjunta by remember { mutableStateOf(false) }

    var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
    var horaInicio by remember { mutableStateOf(LocalTime.of(10, 0)) }
    var horaFin by remember { mutableStateOf(LocalTime.of(11, 30)) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFin by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val textColor = tema.letras

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Actividad", color = tema.fondo) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título de la actividad", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor,
                    focusedBorderColor = tema.botones,
                    unfocusedBorderColor = textColor.copy(alpha = 0.5f)
                )
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor,
                    focusedBorderColor = tema.botones,
                    unfocusedBorderColor = textColor.copy(alpha = 0.5f)
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = esPrivada,
                    onCheckedChange = { esPrivada = it },
                    colors = CheckboxDefaults.colors(checkedColor = tema.botones)
                )
                Text("Es Privada", color = textColor)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = esConjunta,
                    onCheckedChange = { esConjunta = it },
                    colors = CheckboxDefaults.colors(checkedColor = tema.botones)
                )
                Text("Es Conjunta (Actividad de Grupo)", color = textColor)
            }

            // Selector Amigable de Fecha
            val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
            OutlinedTextField(
                value = fechaSeleccionada.format(dateFormatter).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() },
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de la Actividad", color = textColor) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Seleccionar fecha",
                        tint = tema.botones
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(12.dp),
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = textColor,
                    disabledLabelColor = textColor,
                    disabledBorderColor = textColor.copy(alpha = 0.5f),
                    disabledTrailingIconColor = tema.botones
                )
            )

            // Selector Amigable de Hora de Inicio y Hora Fin
            val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = horaInicio.format(timeFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Hora Inicio", color = textColor) },
                    trailingIcon = {
                        Icon(Icons.Default.AccessTime, contentDescription = "Hora inicio", tint = tema.botones)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showTimePickerInicio = true },
                    shape = RoundedCornerShape(12.dp),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = textColor,
                        disabledLabelColor = textColor,
                        disabledBorderColor = textColor.copy(alpha = 0.5f),
                        disabledTrailingIconColor = tema.botones
                    )
                )

                OutlinedTextField(
                    value = horaFin.format(timeFormatter),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Hora Fin", color = textColor) },
                    trailingIcon = {
                        Icon(Icons.Default.AccessTime, contentDescription = "Hora fin", tint = tema.botones)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showTimePickerFin = true },
                    shape = RoundedCornerShape(12.dp),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = textColor,
                        disabledLabelColor = textColor,
                        disabledBorderColor = textColor.copy(alpha = 0.5f),
                        disabledTrailingIconColor = tema.botones
                    )
                )
            }

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }

            Button(
                onClick = {
                    if (titulo.isBlank()) {
                        errorMessage = "El título no puede estar vacío"
                        return@Button
                    }
                    if (horaFin.isBefore(horaInicio)) {
                        errorMessage = "La hora de fin debe ser posterior a la hora de inicio"
                        return@Button
                    }

                    val inicio = LocalDateTime.of(fechaSeleccionada, horaInicio)
                    val fin = LocalDateTime.of(fechaSeleccionada, horaFin)

                    val segmento = SegmentoTiempo(inicio, fin, esConjunta, 0.0)

                    val actividad = if (esConjunta) {
                        ActividadConjunta(0, titulo, descripcion, usuarioActual, esPrivada, segmento)
                    } else {
                        ActividadSolitaria(0, titulo, descripcion, usuarioActual, esPrivada, segmento)
                    }

                    onSaveClick(actividad)
                    onBackClick()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = tema.botones, contentColor = tema.fondo)
            ) {
                Text("Guardar Actividad", color = tema.fondo, fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }

    // Diálogo Selector de Fecha
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = fechaSeleccionada.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        fechaSeleccionada = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = tema.botones)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = tema.letras)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Diálogo Selector de Hora Inicio
    if (showTimePickerInicio) {
        val timePickerState = rememberTimePickerState(
            initialHour = horaInicio.hour,
            initialMinute = horaInicio.minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePickerInicio = false },
            confirmButton = {
                TextButton(onClick = {
                    horaInicio = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePickerInicio = false
                }) {
                    Text("OK", color = tema.botones)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerInicio = false }) {
                    Text("Cancelar", color = tema.letras)
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    // Diálogo Selector de Hora Fin
    if (showTimePickerFin) {
        val timePickerState = rememberTimePickerState(
            initialHour = horaFin.hour,
            initialMinute = horaFin.minute,
            is24Hour = false
        )
        AlertDialog(
            onDismissRequest = { showTimePickerFin = false },
            confirmButton = {
                TextButton(onClick = {
                    horaFin = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimePickerFin = false
                }) {
                    Text("OK", color = tema.botones)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePickerFin = false }) {
                    Text("Cancelar", color = tema.letras)
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
