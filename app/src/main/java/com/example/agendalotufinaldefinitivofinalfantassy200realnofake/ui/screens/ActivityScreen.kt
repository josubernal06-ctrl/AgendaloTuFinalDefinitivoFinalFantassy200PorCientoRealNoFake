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
                modifier = Modifier.navigationBarsPadding()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontSize = 12.sp) },
                        icon = {
                            Icon(
                                tabIcons[index],
                                contentDescription = title,
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
                ExtendedExample("Actividad Simple", onClick = {
                    // Acción Actividad Simple
                })
                ExtendedExample("Actividad en Conjunto", onClick = {
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
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
        text = { Text(text) }
    )
}

@Preview(showBackground = true)
@Composable
fun ActivityScreenPreview() {
    AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
        ActivityScreen(onBackClick = {})
    }
}
