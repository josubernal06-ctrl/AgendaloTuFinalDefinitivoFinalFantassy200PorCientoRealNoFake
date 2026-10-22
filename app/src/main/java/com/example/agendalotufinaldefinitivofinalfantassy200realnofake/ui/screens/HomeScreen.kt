package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.BotonPersonalizado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.CalendarioSemanal
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.FeriadoCard
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.HorarioCard
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.ClimaViewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.HolidayUiState
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.HolidayViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tema: TemaColor,
    onNavigateToProfile: () -> Unit,
    onNavigateToAlarm: () -> Unit = {},
    onNavigateToActivity: () -> Unit = {},
    holidayViewModel: HolidayViewModel = viewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    
    val tabTitles = listOf("Calendar", "Alarm")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Alarm,
    )
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "AgendaloTú",
                tema = tema,
                onBackClick = null
            )
        },
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.navigationBarsPadding(),
                containerColor = tema.botones,
                contentColor = tema.fondo,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = tema.fondo
                        )
                    }
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            if (index == 1) {
                                onNavigateToAlarm()
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        selectedContentColor = tema.fondo,
                        unselectedContentColor = tema.letras.copy(alpha = 0.6f)
                    )
                }
            }
        },
        floatingActionButton = {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ExtendedFloatingActionButton(
                    onClick = onNavigateToActivity,
                    containerColor = tema.botones,
                    contentColor = tema.fondo,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Mis Actividades") },
                    text = { Text("Ver Actividades") }
                )
            }
        },
        containerColor = tema.fondo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalendarioSemanal(
                tema = tema,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                BotonPersonalizado(
                    texto = "Ver Mi Perfil",
                    onClick = onNavigateToProfile,
                    colorFondo = tema.botones,
                    colorTexto = tema.fondo
                )
            }

            Text(
                text = "Tus grupos:",
                color = tema.letras,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Espacio reservado para grupos
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HorarioCard(
                        titulo = "Inglés B2",
                        descripcion = "Práctica de speaking y vocabulario",
                        hora = "4:30 PM - 6:00 PM",
                        tema = tema,
                        modifier = Modifier.width(280.dp)
                    )
                }
            }

            // 5. Título de feriados de Bolivia
            Text(
                text = "Feriados en Bolivia (BO):",
                color = tema.letras,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // 6. Sección deslizable de feriados usando MVVM
            val holidayUiState by holidayViewModel.uiState.collectAsState()

            when (val state = holidayUiState) {
                is HolidayUiState.Loading -> {
                    Text(
                        text = "Cargando feriados de Bolivia...",
                        color = tema.letras.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                is HolidayUiState.Error -> {
                    Text(
                        text = "Error al cargar feriados: ${state.message}",
                        color = tema.letras.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                is HolidayUiState.Success -> {
                    if (state.holidays.isEmpty()) {
                        Text(
                            text = "No se encontraron feriados para Bolivia.",
                            color = tema.letras.copy(alpha = 0.7f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    } else {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.holidays) { feriado ->
                                FeriadoCard(
                                    nombre = feriado.name,
                                    fecha = feriado.date,
                                    tema = tema,
                                    modifier = Modifier.width(220.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 5. Título y vista de Clima debajo de las cards
            Text(
                text = "Clima:",
                color = tema.letras,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            val climaViewModel: ClimaViewModel = viewModel()
            val climaState by climaViewModel.climaState.collectAsState()
            val isLoading by climaViewModel.isLoading.collectAsState()
            val errorState by climaViewModel.errorState.collectAsState()

            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = tema.botones
                    )
                }
                errorState != null -> {
                    Text(
                        text = "No se pudo cargar el clima",
                        color = tema.letras.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                climaState != null -> {
                    val pronostico = climaState?.pronosticoPorHora?.take(12) ?: emptyList()
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(pronostico) { item ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.25f)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .width(70.dp)
                                    .height(82.dp)
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
                                        fontSize = 13.sp,
                                        color = tema.letras
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (item.probabilidadLluvia > 40) Icons.Default.WaterDrop else Icons.Default.WbSunny,
                                            contentDescription = null,
                                            tint = if (item.probabilidadLluvia > 40) tema.botones else tema.letras,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Text(
                                            text = "${item.probabilidadLluvia}%",
                                            fontSize = 10.sp,
                                            color = tema.letras,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Text(
                                        text = item.hora,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = tema.letras
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}