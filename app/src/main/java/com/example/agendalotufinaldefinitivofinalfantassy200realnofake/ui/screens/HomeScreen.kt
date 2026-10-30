package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.CalendarioSemanal
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.ActividadesViewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.GrupoUiState
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.GrupoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tema: TemaColor,
    onNavigateToProfile: () -> Unit,
    onNavigateToAlarm: () -> Unit = {},
    onNavigateToGroups: () -> Unit = {},
    onNavigateToActivity: () -> Unit = {},
    grupoViewModel: GrupoViewModel = viewModel(),
    actividadesViewModel: ActividadesViewModel? = null
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val tabTitles = listOf("Home", "Grupos", "Alarma")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Group,
        Icons.Default.Alarm
    )
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    val actividadesUiState = actividadesViewModel?.uiState?.collectAsState()?.value
    val actividadesList = actividadesUiState?.actividades ?: emptyList()

    val actividadesDelDia = actividadesList.filter { act ->
        try {
            val inicioStr = act.segmentoTiempo.inicio.toString()
            if (inicioStr.contains("T")) {
                LocalDate.parse(inicioStr.substringBefore("T")) == selectedDate
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    val grupoUiState by grupoViewModel.uiState.collectAsState()
    val gruposList = (grupoUiState as? GrupoUiState.Success)?.grupos ?: emptyList()

    val formattedDateText = remember(selectedDate) {
        selectedDate.format(DateTimeFormatter.ofPattern("d 'de' MMM", Locale("es", "ES")))
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MiTopBar(
                titulo = "AgendaloTú",
                tema = tema,
                onPerfilClick = onNavigateToProfile,
                onSettingsClick = {},
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.navigationBarsPadding(),
                containerColor = tema.botones,
                contentColor = Color.White,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color.White
                        )
                    }
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index
                    Tab(
                        selected = isSelected,
                        onClick = {
                            selectedTabIndex = index
                            if (index == 1) {
                                onNavigateToGroups()
                            } else if (index == 2) {
                                onNavigateToAlarm()
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = Color.White
                            )
                        },
                        icon = {
                            Surface(
                                color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = tabIcons[index],
                                        contentDescription = title,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToActivity,
                containerColor = tema.botones,
                contentColor = tema.fondo,
                icon = { Icon(Icons.Default.Add, contentDescription = "Nueva Actividad") },
                text = { Text("Nueva Actividad") }
            )
        },
        containerColor = tema.fondo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            CalendarioSemanal(
                tema = tema,
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Actividades · $formattedDateText",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = tema.letras
                    )
                    Text(
                        text = "Ver todas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = tema.botones,
                        modifier = Modifier.clickable { onNavigateToActivity() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (actividadesDelDia.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToActivity() },
                        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.EventNote,
                                contentDescription = null,
                                tint = tema.letras.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "No hay actividades para este día",
                                fontSize = 14.sp,
                                color = tema.letras.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        actividadesDelDia.forEach { actividad ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToActivity() },
                                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = actividad.titulo,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = tema.letras
                                    )
                                    if (actividad.descripcion.isNotBlank()) {
                                        Text(
                                            text = actividad.descripcion,
                                            fontSize = 13.sp,
                                            color = tema.letras.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tus grupos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = tema.letras
                    )
                    Text(
                        text = "Ver todos",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = tema.botones,
                        modifier = Modifier.clickable { onNavigateToGroups() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (gruposList.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToGroups() },
                        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = tema.letras.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Aún no perteneces a ningún grupo",
                                fontSize = 14.sp,
                                color = tema.letras.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(gruposList) { grupo ->
                            Card(
                                modifier = Modifier
                                    .width(260.dp)
                                    .clickable { onNavigateToGroups() },
                                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        color = tema.botones.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = tema.botones
                                            )
                                        }
                                    }
                                    Column {
                                        Text(
                                            text = grupo.nombreGrupo,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = tema.letras
                                        )
                                        Text(
                                            text = "${grupo.descripcion.ifBlank { "Estudios" }} · ${grupo.miebros.size} miembro${if (grupo.miebros.size != 1) "s" else ""}",
                                            fontSize = 12.sp,
                                            color = tema.letras.copy(alpha = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
