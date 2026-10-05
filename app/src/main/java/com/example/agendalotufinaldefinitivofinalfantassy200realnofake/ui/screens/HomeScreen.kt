package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Inicio",
                tema = tema,
                onBackClick = null // No necesitamos volver atrás desde Home
            )
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                
                // Botón para ir al perfil
                BotonPersonalizado(
                    texto = "Ver Mi Perfil",
                    onClick = onNavigateToProfile,
                    colorFondo = tema.botones,
                    colorTexto = tema.fondo
                )
            }

            item {
                Text(
                    text = "Tus próximas actividades:",
                    color = tema.letras,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Tarjetas de horarios de ejemplo
            item {
                HorarioCard(
                    titulo = "Desarrollo de Apps",
                    descripcion = "Revisión de Jetpack Compose y estado",
                    hora = "10:00 AM - 11:30 AM",
                    tema = tema
                )
            }

            item {
                HorarioCard(
                    titulo = "Reunión de Proyecto",
                    descripcion = "Sincronización del equipo de backend",
                    hora = "2:00 PM - 3:00 PM",
                    tema = tema
                )
            }

            item {
                HorarioCard(
                    titulo = "Inglés B2",
                    descripcion = "Práctica de speaking y vocabulario",
                    hora = "4:30 PM - 6:00 PM",
                    tema = tema
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp)) // Espacio extra al final para que el FAB no tape el contenido
            }
        }
    }
}
