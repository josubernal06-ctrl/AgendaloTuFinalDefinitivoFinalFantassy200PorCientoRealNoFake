package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.ActividadConjunta
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.CalendarioSemanal
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.HorarioCard
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupActivityCalendarScreen(
    tema: TemaColor,
    grupo: Grupo,
    actividades: List<ActividadConjunta>,
    onBackClick: () -> Unit,
    onNavigateToCreateActivity: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Calendario: ${grupo.nombreGrupo}",
                tema = tema,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigateToCreateActivity(grupo.id) },
                containerColor = tema.botones,
                contentColor = tema.fondo,
                icon = { Icon(Icons.Default.Add, contentDescription = "Proponer Actividad") },
                text = { Text("Proponer Actividad") }
            )
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
            CalendarioSemanal(tema = tema)
            
            Text(
                text = "Próximas Actividades",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = tema.letras
            )
            
            val actividadesDelGrupo = actividades.filter { it.grupoId == grupo.id }
            
            if (actividadesDelGrupo.isEmpty()) {
                Text(
                    text = "No hay actividades programadas para este grupo.",
                    color = tema.letras.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(actividadesDelGrupo) { actividad ->
                        HorarioCard(
                            titulo = actividad.titulo,
                            descripcion = actividad.descripcion,
                            hora = "${actividad.segmentoTiempo.inicio.toLocalTime()} - ${actividad.segmentoTiempo.fin.toLocalTime()}",
                            tema = tema,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
