package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
    var fechaInicio by remember { mutableStateOf("2024-12-31T10:00") }
    var fechaFin by remember { mutableStateOf("2024-12-31T12:00") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val textColor = Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Actividad", color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = textColor)
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
                Checkbox(checked = esPrivada, onCheckedChange = { esPrivada = it })
                Text("Es Privada", color = textColor)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = esConjunta, onCheckedChange = { esConjunta = it })
                Text("Es Conjunta (Actividad de Grupo)", color = textColor)
            }

            OutlinedTextField(
                value = fechaInicio,
                onValueChange = { fechaInicio = it },
                label = { Text("Inicio (Formato YYYY-MM-DDTHH:MM)", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
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
                value = fechaFin,
                onValueChange = { fechaFin = it },
                label = { Text("Fin (Formato YYYY-MM-DDTHH:MM)", color = textColor) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedLabelColor = textColor,
                    unfocusedLabelColor = textColor,
                    focusedBorderColor = tema.botones,
                    unfocusedBorderColor = textColor.copy(alpha = 0.5f)
                )
            )

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (titulo.isBlank()) {
                        errorMessage = "El título no puede estar vacío"
                        return@Button
                    }
                    try {
                        val inicio = LocalDateTime.parse(fechaInicio, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        val fin = LocalDateTime.parse(fechaFin, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        
                        val segmento = SegmentoTiempo(inicio, fin, esConjunta, 0.0)
                        
                        val actividad = if (esConjunta) {
                            ActividadConjunta(0, titulo, descripcion, usuarioActual, esPrivada, segmento)
                        } else {
                            ActividadSolitaria(0, titulo, descripcion, usuarioActual, esPrivada, segmento)
                        }
                        
                        onSaveClick(actividad)
                        onBackClick()
                    } catch (e: DateTimeParseException) {
                        errorMessage = "Formato de fecha inválido. Usa YYYY-MM-DDTHH:MM (ej. 2024-12-31T10:00)"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = tema.botones, contentColor = Color.White)
            ) {
                Text("Guardar Actividad", color = Color.White)
            }
        }
    }
}
