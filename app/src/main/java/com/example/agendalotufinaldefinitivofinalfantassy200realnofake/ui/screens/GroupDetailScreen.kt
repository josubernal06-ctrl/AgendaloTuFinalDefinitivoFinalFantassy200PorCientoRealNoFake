package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    tema: TemaColor,
    grupoId: Int, // En la realidad, cargaríamos el grupo desde el repositorio usando el ID
    grupos: List<Grupo>, // Por simplicidad, pasamos la lista actual y filtramos
    onBackClick: () -> Unit,
    onNavigateToCalendar: (Int) -> Unit
) {
    val grupo = grupos.find { it.id == grupoId }
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    if (grupo == null) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Grupo no encontrado", color = tema.letras)
            Button(onClick = onBackClick) { Text("Volver") }
        }
        return
    }

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = grupo.nombreGrupo,
                tema = tema,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNavigateToCalendar(grupo.id) },
                containerColor = tema.botones,
                contentColor = tema.fondo,
                icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Calendario Grupal") },
                text = { Text("Calendario") }
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
            if (grupo.descripcion.isNotBlank()) {
                Text(text = "Descripción", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = tema.letras)
                Text(text = grupo.descripcion, color = tema.letras.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = "Código de Invitación", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = tema.letras)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = grupo.codigoInvitacion, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = tema.botones, modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString(grupo.codigoInvitacion))
                    copied = true
                }) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copiar", tint = tema.botones)
                }
            }
            if (copied) {
                Text("¡Copiado al portapapeles!", color = tema.detalles, fontSize = 12.sp)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Integrantes (${grupo.miebros.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = tema.letras)
            
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(grupo.miebros) { miembro ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.1f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(text = miembro.nickname, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = tema.letras)
                            if (miembro.id == grupo.propietario.id) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = tema.botones,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                                ) {
                                    Text("Admin", color = tema.fondo, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
