package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
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
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.CalendarGridView
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema

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
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                ExtendedExample("Actividad Simple", tema = tema, onClick = {
                    // Acción Actividad Simple
                })
                ExtendedExample("Actividad en Conjunto", tema = tema, onClick = {
                    // Acción Actividad en Conjunto
                })
            }
        },
        containerColor = tema.fondo
    ) { innerPadding ->
        CalendarGridView(innerPadding)
    }
}

@Composable
fun ExtendedExample(
    text: String,
    tema: TemaColor,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = tema.botones,
        contentColor = tema.letras,
        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = tema.letras) },
        text = { Text(text, color = tema.letras) }
    )
}

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
        ActivityScreen(onBackClick = {})
    }
}
