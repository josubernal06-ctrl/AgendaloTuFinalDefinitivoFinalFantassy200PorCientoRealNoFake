package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Clima
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.HoraClima
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Holiday
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.CalendarGridView
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.ActividadUiState
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.AiViewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.ClimaViewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.HolidayUiState
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.HolidayViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    onBackClick: () -> Unit,
    aiViewModel: AiViewModel = viewModel(),
    holidayViewModel: HolidayViewModel = viewModel(),
    climaViewModel: ClimaViewModel = viewModel(),
    tema: TemaColor = obtenerTema(1),
    uiState: ActividadUiState? = null,
    onNavigateToCreate: () -> Unit = {},
    onDeleteActivity: (AbstractActividad) -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }
    val context = LocalContext.current

    val promptText by aiViewModel.promptText.collectAsState()
    val responseText by aiViewModel.responseText.collectAsState()
    val isLoading by aiViewModel.isLoading.collectAsState()

    val holidayUiState by holidayViewModel.uiState.collectAsState()
    val holidaysList = (holidayUiState as? HolidayUiState.Success)?.holidays ?: emptyList()
    val actividadesList = uiState?.actividades ?: emptyList()

    val climaState by climaViewModel.climaState.collectAsState()
    val isClimaLoading by climaViewModel.isLoading.collectAsState()
    val climaError by climaViewModel.errorState.collectAsState()

    // Filtrar feriados para la fecha seleccionada
    val feriadosDelDia = holidaysList.filter { feriado ->
        try {
            LocalDate.parse(feriado.date) == selectedDate
        } catch (_: Exception) {
            false
        }
    }

    // Filtrar actividades del usuario para la fecha seleccionada
    val actividadesDelDia = actividadesList.filter { act ->
        try {
            act.segmentoTiempo.inicio.toLocalDate() == selectedDate
        } catch (_: Exception) {
            false
        }
    }

    val formattedSelectedDate = selectedDate.format(
        DateTimeFormatter.ofPattern("EEE, d 'de' MMMM", Locale("es", "ES"))
    ).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Actividades", color = tema.fondo) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = tema.fondo
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = tema.botones
                )
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
                .imePadding()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Calendario mensual interactivo
            CalendarGridView(
                currentYearMonth = currentYearMonth,
                onYearMonthChange = { currentYearMonth = it },
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                actividades = actividadesList,
                holidays = holidaysList,
                tema = tema
            )

            // Leyenda explicativa de colores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(12.dp, 4.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFF22C55E)))
                    Text("Feriados", fontSize = 12.sp, color = tema.letras, fontWeight = FontWeight.Medium)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(12.dp, 4.dp).clip(RoundedCornerShape(2.dp)).background(Color(0xFF3B82F6)))
                    Text("Actividades", fontSize = 12.sp, color = tema.letras, fontWeight = FontWeight.Medium)
                }
            }

            // 2. Título de la fecha seleccionada
            Text(
                text = formattedSelectedDate,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = tema.letras
            )

            // 3. Clima para el día seleccionado
            ClimaDetalleCard(
                climaState = climaState,
                isLoading = isClimaLoading,
                errorState = climaError,
                selectedDate = selectedDate,
                tema = tema
            )

            // 4. Feriados en el día seleccionado
            feriadosDelDia.forEach { feriado ->
                FeriadoDetalleCard(feriado = feriado, tema = tema)
            }

            // 5. Actividades en el día seleccionado
            if (actividadesDelDia.isNotEmpty()) {
                actividadesDelDia.forEach { actividad ->
                    ActividadItem(
                        actividad = actividad,
                        tema = tema,
                        onDeleteClick = { onDeleteActivity(actividad) }
                    )
                }
            } else if (feriadosDelDia.isEmpty()) {
                Text(
                    text = "No hay actividades ni feriados agendados para este día.",
                    fontSize = 14.sp,
                    color = tema.letras.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 6. Asistente IA Gemini
            Text("Asistente IA Gemini:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = tema.letras)

            OutlinedTextField(
                value = promptText,
                onValueChange = { aiViewModel.onPromptChanged(it) },
                label = { Text("Ej: Agendar reunión mañana a las 4 PM", color = tema.letras) },
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

            Button(
                onClick = {
                    aiViewModel.enviarPromptAGemini(context = context)
                },
                colors = ButtonDefaults.buttonColors(containerColor = tema.botones),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = tema.fondo)
                } else {
                    Text("Agendar con IA", color = tema.fondo)
                }
            }

            if (responseText.isNotEmpty() || isLoading) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = tema.detalles.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, tema.botones.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Asistente IA",
                                tint = tema.botones,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Respuesta de Gemini",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = tema.botones
                            )
                        }

                        if (isLoading) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = tema.botones,
                                    strokeWidth = 2.dp
                                )
                                Text(
                                    text = "Procesando con Gemini IA...",
                                    fontSize = 14.sp,
                                    color = tema.letras.copy(alpha = 0.8f)
                                )
                            }
                        } else {
                            Text(
                                text = responseText,
                                color = tema.letras,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun ClimaDetalleCard(
    climaState: Clima?,
    isLoading: Boolean,
    errorState: String?,
    selectedDate: LocalDate,
    tema: TemaColor
) {
    val pronosticoDelDia = climaState?.pronosticoPorHora?.take(12) ?: emptyList()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF97316).copy(alpha = 0.12f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Thermostat,
                        contentDescription = "Clima",
                        tint = Color(0xFFEA580C),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Clima para ${selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMM", Locale("es", "ES")))}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = tema.letras
                    )
                }

                if (pronosticoDelDia.isNotEmpty()) {
                    val tempPromedio = pronosticoDelDia.map { it.temperatura }.average().toInt()
                    Text(
                        text = "$tempPromedio°C",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA580C)
                    )
                }
            }

            when {
                isLoading -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color(0xFFEA580C),
                            strokeWidth = 2.dp
                        )
                        Text("Obteniendo clima...", fontSize = 12.sp, color = tema.letras.copy(alpha = 0.7f))
                    }
                }
                errorState != null -> {
                    Text(
                        text = "Error al obtener clima: $errorState",
                        fontSize = 12.sp,
                        color = tema.letras.copy(alpha = 0.6f)
                    )
                }
                pronosticoDelDia.isNotEmpty() -> {
                    val maxLluvia = pronosticoDelDia.maxOfOrNull { it.probabilidadLluvia } ?: 0
                    val maxTemp = pronosticoDelDia.maxOfOrNull { it.temperatura }?.toInt() ?: 0
                    val minTemp = pronosticoDelDia.minOfOrNull { it.temperatura }?.toInt() ?: 0

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Máx: ${maxTemp}°C  |  Mín: ${minTemp}°C",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = tema.letras.copy(alpha = 0.8f)
                        )
                        Text(
                            text = if (maxLluvia > 40) "Prob. Lluvia: $maxLluvia%" else "Día Soleado / Despejado",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (maxLluvia > 40) Color(0xFF0284C7) else Color(0xFFD97706)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(count = pronosticoDelDia.size) { index ->
                            val item = pronosticoDelDia[index]
                            Card(
                                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.25f)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .width(62.dp)
                                    .height(70.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(
                                        text = "${item.temperatura.toInt()}°C",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = tema.letras
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (item.probabilidadLluvia > 40) Icons.Default.WaterDrop else Icons.Default.WbSunny,
                                            contentDescription = null,
                                            tint = if (item.probabilidadLluvia > 40) Color(0xFF0284C7) else Color(0xFFD97706),
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "${item.probabilidadLluvia}%",
                                            fontSize = 9.sp,
                                            color = tema.letras
                                        )
                                    }
                                    Text(
                                        text = item.hora,
                                        fontSize = 10.sp,
                                        color = tema.letras.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    Text(
                        text = "Pronóstico no disponible para esta fecha.",
                        fontSize = 12.sp,
                        color = tema.letras.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun FeriadoDetalleCard(
    feriado: Holiday,
    tema: TemaColor
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF22C55E).copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Celebration,
                contentDescription = "Feriado",
                tint = Color(0xFF16A34A),
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = feriado.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = tema.letras
                )
                Text(
                    text = "Feriado Público Nacional",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF16A34A)
                )
            }
        }
    }
}

@Composable
fun ActividadItem(
    actividad: AbstractActividad,
    tema: TemaColor,
    onDeleteClick: () -> Unit
) {
    val inicioTime = try {
        actividad.segmentoTiempo.inicio.toLocalTime().toString().take(5)
    } catch (_: Exception) { "" }

    val finTime = try {
        actividad.segmentoTiempo.fin.toLocalTime().toString().take(5)
    } catch (_: Exception) { "" }

    val segmentoString = if (inicioTime.isNotEmpty() && finTime.isNotEmpty()) {
        "$inicioTime - $finTime"
    } else {
        "Horario no especificado"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3B82F6).copy(alpha = 0.15f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.EventNote,
                        contentDescription = "Actividad",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = actividad.titulo,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = tema.letras
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar actividad",
                        tint = tema.letras
                    )
                }
            }

            if (actividad.descripcion.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = actividad.descripcion,
                    fontSize = 14.sp,
                    color = tema.letras.copy(alpha = 0.8f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Visibilidad: ${if (actividad.esPrivada) "Privada" else "Pública"}",
                    fontSize = 13.sp,
                    color = tema.letras.copy(alpha = 0.7f)
                )
                Text(
                    text = segmentoString,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB)
                )
            }
        }
    }
}
