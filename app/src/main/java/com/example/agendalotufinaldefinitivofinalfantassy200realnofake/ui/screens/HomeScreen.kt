package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.BotonPersonalizado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.Example
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.HorarioCard
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    tema: TemaColor,
    onNavigateToProfile: () -> Unit
) {
    var presses by remember { mutableIntStateOf(0) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val searchState = remember { TextFieldState() }
    var results by remember { mutableStateOf(listOf<String>()) }
    val tabTitles = listOf("Calendar", "Alarm")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Alarm,
    )
    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Inicio",
                tema = tema,
                onBackClick = null // No necesitamos volver atrás desde Home
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
            Example(
                onClick = { /* TODO: Acción para añadir nuevo horario */ },
                colorFondo = tema.botones,
                colorTexto = tema.fondo
            )
        },
        containerColor = tema.fondo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {



            Spacer(modifier = Modifier.height(162.dp))
            Column {
                // Botón de Perfil en la parte superior
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

                // Espaciador controlado para separación estética
                Spacer(modifier = Modifier.height(26.dp))

                // Título de próximas actividades
                Text(
                    text = "Tus próximas actividades:",
                    color = tema.letras,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        HorarioCard(
                            titulo = "Desarrollo de Apps",
                            descripcion = "Revisión de Jetpack Compose y estado",
                            hora = "10:00 AM - 11:30 AM",
                            tema = tema,
                            modifier = Modifier.width(280.dp)
                        )
                    }

                    item {
                        HorarioCard(
                            titulo = "Reunión de Proyecto",
                            descripcion = "Sincronización del equipo de backend",
                            hora = "2:00 PM - 3:00 PM",
                            tema = tema,
                            modifier = Modifier.width(280.dp)
                        )
                    }

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
            }
        }
    }
}
