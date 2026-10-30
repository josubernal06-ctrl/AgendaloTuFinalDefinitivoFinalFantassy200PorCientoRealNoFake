package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Group
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
    var selectedTabIndex by remember { mutableIntStateOf(1) } // 1 = Grupos
    val tabTitles = listOf("Home", "Grupos", "Alarma")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Group,
        Icons.Default.Alarm
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
                contentColor = androidx.compose.ui.graphics.Color.White,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = androidx.compose.ui.graphics.Color.White
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
                            if (index == 0) onNavigateToHome()
                            if (index == 2) onNavigateToAlarm()
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = androidx.compose.ui.graphics.Color.White
                            )
                        },
                        icon = {
                            Surface(
                                color = if (isSelected) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.25f) else androidx.compose.ui.graphics.Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = tabIcons[index],
                                        contentDescription = title,
                                        tint = androidx.compose.ui.graphics.Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        },
                        selectedContentColor = androidx.compose.ui.graphics.Color.White,
                        unselectedContentColor = androidx.compose.ui.graphics.Color.White
                    )
                }
            }
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ExtendedFloatingActionButton(
                    onClick = onJoinGroup,
                    containerColor = tema.botones,
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
        when (uiState) {
            is GrupoUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = tema.botones)
                }
            }
            is GrupoUiState.Success -> {
                if (uiState.grupos.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No estás en ningún grupo todavía.", color = tema.letras)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.grupos) { grupo ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onGroupClick(grupo) },
                                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.2f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = grupo.nombreGrupo,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = tema.letras
                                    )
                                    if (grupo.descripcion.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = grupo.descripcion,
                                            fontSize = 14.sp,
                                            color = tema.letras.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is GrupoUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
