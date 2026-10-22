package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.CalendarGridView
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.AiViewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.ActividadUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    onBackClick: () -> Unit,
    aiViewModel: AiViewModel = viewModel()
    tema: TemaColor,
    uiState: ActividadUiState,
    onBackClick: () -> Unit,
    onNavigateToCreate: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Calendar", "Alarm")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Alarm,
    )
    val tema = obtenerTema(1)

    val promptText by aiViewModel.promptText.collectAsState()
    val responseText by aiViewModel.responseText.collectAsState()
    val isLoading by aiViewModel.isLoading.collectAsState()

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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalendarGridView(PaddingValues(0.dp))

            // Text field para promptear a gemini
            OutlinedTextField(
                value = promptText,
                onValueChange = { aiViewModel.onPromptChanged(it) },
                label = { Text("Prompt para Gemini", color = tema.letras) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = tema.botones,
                    unfocusedBorderColor = tema.letras.copy(alpha = 0.5f),
                    focusedLabelColor = tema.letras,
                    unfocusedLabelColor = tema.letras.copy(alpha = 0.7f),
                    focusedTextColor = tema.letras,
                    unfocusedTextColor = tema.letras
                )
            )

            // Boton "Agendar con AI"
            Button(
                onClick = { aiViewModel.enviarPromptAGemini() },
                colors = ButtonDefaults.buttonColors(containerColor = tema.botones),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = tema.letras)
                } else {
                    Text("Agendar con AI", color = tema.letras)
                }
            }
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

            // Text donde va la respuesta de la API (lazy / scrollable)
            Text("Respuesta de la IA:", color = tema.letras, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = responseText.ifEmpty { "Aquí aparecerá la respuesta de Gemini..." },
                    color = tema.letras,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botones "Actividad Simple" y "Actividad en Conjunto" uno al lado del otro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { /* Acción Actividad Simple */ },
                    colors = ButtonDefaults.buttonColors(containerColor = tema.botones),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = tema.letras, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Actividad Simple", color = tema.letras, fontSize = 12.sp)
                }
                Button(
                    onClick = { /* Acción Actividad en Conjunto */ },
                    colors = ButtonDefaults.buttonColors(containerColor = tema.botones),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = tema.letras, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Actividad Conjunta", color = tema.letras, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
        ActivityScreen(onBackClick = {})
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
