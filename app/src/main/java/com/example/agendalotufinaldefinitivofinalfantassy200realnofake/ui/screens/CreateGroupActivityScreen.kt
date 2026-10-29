package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.BotonPersonalizado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.EntradaTextoField
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupActivityScreen(
    tema: TemaColor,
    grupoId: Int,
    usuarioActual: Usuario,
    onBackClick: () -> Unit,
    onSaveClick: (titulo: String, desc: String, recomendacion: String) -> Unit,
    aiRecommendationState: String,
    onSuggestClick: (prompt: String) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var promptAdicional by remember { mutableStateOf("") }
    
    var showRecommendationDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Proponer Actividad",
                tema = tema,
                onBackClick = onBackClick
            )
        },
        containerColor = tema.fondo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Detalles de la Actividad", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = tema.letras)
            
            EntradaTextoField(
                valor = titulo,
                onValueChange = { titulo = it },
                label = "Título *",
                tema = tema,
                modifier = Modifier.fillMaxWidth()
            )
            
            EntradaTextoField(
                valor = descripcion,
                onValueChange = { descripcion = it },
                label = "Descripción",
                tema = tema,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Dejar a Gemini decidir el horario", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = tema.letras)
            
            EntradaTextoField(
                valor = promptAdicional,
                onValueChange = { promptAdicional = it },
                label = "Preferencias (ej. fin de semana, por la tarde)",
                tema = tema,
                modifier = Modifier.fillMaxWidth()
            )
            
            Button(
                onClick = {
                    onSuggestClick("Para la actividad '$titulo', preferencias: $promptAdicional")
                    showRecommendationDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = tema.botones, contentColor = tema.fondo)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recomendar Horarios")
            }
        }
    }
    
    if (showRecommendationDialog) {
        AlertDialog(
            onDismissRequest = { showRecommendationDialog = false },
            title = { Text("Recomendación de Gemini", color = tema.letras) },
            text = { 
                if (aiRecommendationState.isBlank()) {
                    CircularProgressIndicator(color = tema.botones)
                } else {
                    Text(aiRecommendationState, color = tema.letras)
                }
            },
            confirmButton = {
                if (aiRecommendationState.isNotBlank()) {
                    TextButton(onClick = {
                        showRecommendationDialog = false
                        onSaveClick(titulo, descripcion, aiRecommendationState)
                    }) {
                        Text("Aceptar y Guardar", color = tema.botones)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showRecommendationDialog = false }) {
                    Text("Cancelar", color = tema.letras)
                }
            },
            containerColor = tema.fondo
        )
    }
}
