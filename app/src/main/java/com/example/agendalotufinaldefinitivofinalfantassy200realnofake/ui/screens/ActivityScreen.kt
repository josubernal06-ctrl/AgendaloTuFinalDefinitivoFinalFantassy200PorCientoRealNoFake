package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.CalendarGridView
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    onBackClick: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Calendar", "Alarm")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Alarm,
    )
    val tema = obtenerTema(1)

    var promptText by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val aiRepository = remember { AiRepository() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Actividades", color = tema.letras) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = tema.letras
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = tema.botones,
                )
            )
        },
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = tema.botones,
                contentColor = tema.letras,
                modifier = Modifier.navigationBarsPadding()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                color = if (isSelected) tema.letras else tema.letras.copy(alpha = 0.6f)
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = title,
                                tint = if (isSelected) tema.letras else tema.letras.copy(alpha = 0.6f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    )
                }
            }
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
                onValueChange = { promptText = it },
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
                onClick = {
                    if (promptText.isNotBlank()) {
                        scope.launch {
                            isLoading = true
                            try {
                                val res = aiRepository.obtenerRespuestaGemini(promptText)
                                responseText = res.answer
                            } catch (e: Exception) {
                                responseText = "Error al conectar con AI: ${e.localizedMessage}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = tema.botones),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = tema.letras)
                } else {
                    Text("Agendar con AI", color = tema.letras)
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
    }
}
