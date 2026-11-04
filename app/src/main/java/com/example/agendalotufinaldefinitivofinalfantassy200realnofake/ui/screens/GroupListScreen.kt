package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.GrupoUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    tema: TemaColor,
    uiState: GrupoUiState,
    onNavigateToHome: () -> Unit,
    onNavigateToAlarm: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
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
                onSettingsClick = onNavigateToSettings
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
        containerColor = tema.fondo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Acciones superiores principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCreateGroup,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tema.botones,
                        contentColor = tema.fondo
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = tema.fondo,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Crear Grupo", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = tema.fondo)
                }

                Button(
                    onClick = onJoinGroup,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = tema.botones,
                        contentColor = tema.fondo
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = tema.fondo,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Unirse a Grupo", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = tema.fondo)
                }
            }

            Text(
                text = "Grupos Activos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = tema.letras
            )

            // Contenido de Grupos
            when (uiState) {
                is GrupoUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = tema.botones)
                    }
                }
                is GrupoUiState.Success -> {
                    if (uiState.grupos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aún no perteneces a ningún grupo. ¡Crea uno o únete con un código!",
                                color = tema.letras.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.grupos) { grupo ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onGroupClick(grupo) },
                                    colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.2f)),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = grupo.nombreGrupo,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 17.sp,
                                                color = tema.letras
                                            )
                                            if (grupo.descripcion.isNotEmpty()) {
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = grupo.descripcion,
                                                    fontSize = 13.sp,
                                                    color = tema.letras.copy(alpha = 0.8f)
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "${grupo.miebros.size} miembro${if (grupo.miebros.size != 1) "s" else ""}",
                                                fontSize = 12.sp,
                                                color = tema.botones,
                                                fontWeight = FontWeight.SemiBold
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
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupListScreenPreview() {
    GroupListScreen(
        tema = obtenerTema(1),
        uiState = GrupoUiState.Success(emptyList()),
        onNavigateToHome = {},
        onNavigateToAlarm = {},
        onCreateGroup = {},
        onJoinGroup = {},
        onGroupClick = {}
    )
}
