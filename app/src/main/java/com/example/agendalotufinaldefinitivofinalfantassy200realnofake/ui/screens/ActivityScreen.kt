package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.ActividadUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    tema: TemaColor,
    uiState: ActividadUiState,
    onBackClick: () -> Unit,
    onNavigateToCreate: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Mis Actividades", color = tema.letras) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = tema.letras)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = tema.botones)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = tema.botones,
                contentColor = tema.fondo,
                icon = { Icon(Icons.Default.Add, contentDescription = "Nueva Actividad") },
                text = { Text("Nueva Actividad") }
            )
        },
        containerColor = tema.fondo
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                Text(text = "Cargando actividades...", color = tema.letras)
            } else if (uiState.error != null) {
                Text(text = "Error: ${uiState.error}", color = MaterialTheme.colorScheme.error)
            } else if (uiState.actividades.isEmpty()) {
                Text(text = "No tienes actividades guardadas. ¡Crea una!", color = tema.letras)
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.actividades) { actividad ->
                        ActividadItem(actividad, tema)
                    }
                }
            }
        }
    }
}

@Composable
fun ActividadItem(actividad: AbstractActividad, tema: TemaColor) {
    // Usamos reflexión simple para leer el valor privado 'esPrivada' para la UI de forma segura
    var esPrivada by remember { mutableStateOf(false) }
    var segmentoString by remember { mutableStateOf("") }
    
    LaunchedEffect(actividad) {
        try {
            val clazz = AbstractActividad::class.java
            val privField = clazz.getDeclaredField("esPrivada")
            privField.isAccessible = true
            esPrivada = privField.get(actividad) as Boolean
            
            // Tratamos de leer el segmento solo para mostrar fechas de manera rudimentaria
            val segField = clazz.getDeclaredField("segmentoTiempo")
            segField.isAccessible = true
            val segmento = segField.get(actividad)
            
            val segClazz = segmento!!::class.java
            val iniField = segClazz.getDeclaredField("inicio")
            iniField.isAccessible = true
            val inicio = iniField.get(segmento).toString().substringBefore("T")
            
            segmentoString = inicio
        } catch (e: Exception) {
            segmentoString = "Fecha oculta"
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = tema.botones.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = actividad.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = tema.letras
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Visibilidad: ${if (esPrivada) "Privada" else "Pública"}",
                    fontSize = 14.sp,
                    color = tema.letras.copy(alpha = 0.8f)
                )
                Text(
                    text = segmentoString,
                    fontSize = 14.sp,
                    color = tema.letras.copy(alpha = 0.8f)
                )
            }
        }
    }
}
