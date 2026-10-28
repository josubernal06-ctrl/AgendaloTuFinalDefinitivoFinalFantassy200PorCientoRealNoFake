package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.GrupoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    tema: TemaColor,
    uiState: GrupoUiState,
    onNavigateToHome: () -> Unit,
    onNavigateToAlarm: () -> Unit,
    onCreateGroup: () -> Unit,
    onJoinGroup: () -> Unit,
    onGroupClick: (Grupo) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(2) } // 2 = Grupos
    val tabTitles = listOf("Calendar", "Alarm", "Grupos")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Alarm,
        Icons.Default.Person
    )

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Mis Grupos",
                tema = tema,
                onBackClick = null,
                showSearchBar = true
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
                            if (index == 0) onNavigateToHome()
                            if (index == 1) onNavigateToAlarm()
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
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExtendedFloatingActionButton(
                    onClick = onJoinGroup,
                    containerColor = tema.detalles,
                    contentColor = tema.fondo,
                    icon = { Icon(Icons.Default.PersonAdd, contentDescription = "Unirse a grupo") },
                    text = { Text("Unirse a grupo") }
                )
                ExtendedFloatingActionButton(
                    onClick = onCreateGroup,
                    containerColor = tema.botones,
                    contentColor = tema.fondo,
                    icon = { Icon(Icons.Default.Add, contentDescription = "Crear grupo") },
                    text = { Text("Crear grupo") }
                )
            }
        },
        containerColor = tema.fondo
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (uiState) {
                is GrupoUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = tema.botones)
                }
                is GrupoUiState.Error -> {
                    Text(text = uiState.message, color = tema.letras, modifier = Modifier.align(Alignment.Center))
                }
                is GrupoUiState.Success -> {
                    if (uiState.grupos.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Aún no perteneces a ningún grupo",
                                color = tema.letras,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Crea tu primer grupo o únete a uno existente usando un código de invitación.",
                                color = tema.letras.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.grupos) { grupo ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onGroupClick(grupo) },
                                    colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f)),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = grupo.nombreGrupo, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = tema.botones)
                                        if (grupo.descripcion.isNotBlank()) {
                                            Text(text = grupo.descripcion, fontSize = 14.sp, color = tema.letras.copy(alpha = 0.7f))
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(text = "${grupo.miebros.size} miembros", fontSize = 12.sp, color = tema.letras)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
