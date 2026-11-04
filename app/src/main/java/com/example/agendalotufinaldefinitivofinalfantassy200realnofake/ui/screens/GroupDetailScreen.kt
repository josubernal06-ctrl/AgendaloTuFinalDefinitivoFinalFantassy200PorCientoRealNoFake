package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.ActividadConjunta
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Grupo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import androidx.compose.ui.tooling.preview.Preview
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    tema: TemaColor,
    grupoId: Int,
    grupos: List<Grupo>,
    actividades: List<ActividadConjunta> = emptyList(),
    currentUserId: String,
    onBackClick: () -> Unit,
    onNavigateToCalendar: (Int) -> Unit,
    onDeleteGroupClick: ((Grupo) -> Unit)? = null,
    onLeaveGroupClick: ((Grupo) -> Unit)? = null,
    onEditActivityClick: ((ActividadConjunta) -> Unit)? = null,
    onAiScheduleClick: (String) -> Unit = {}
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

    val integrantesList = if (grupo.miebros.isNotEmpty()) grupo.miebros else listOf(grupo.propietario)
    var showDeleteGroupDialog by remember { mutableStateOf(false) }
    var aiPrompt by remember { mutableStateOf("") }
    var showAiDialog by remember { mutableStateOf(false) }

    var showLeaveGroupDialog by remember { mutableStateOf(false) }
    var showOwnerLeaveWarningDialog by remember { mutableStateOf(false) }
    
    val isOwner = grupo.propietario.email == currentUserId || grupo.propietario.id.toString() == currentUserId

    if (showDeleteGroupDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteGroupDialog = false },
            title = { Text("Eliminar Grupo", color = tema.letras) },
            text = { Text("¿Estás seguro de que deseas eliminar el grupo \"${grupo.nombreGrupo}\"?", color = tema.letras) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteGroupDialog = false
                        onDeleteGroupClick?.invoke(grupo)
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = Color.White)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteGroupDialog = false }) {
                    Text("Cancelar", color = tema.letras)
                }
            },
            containerColor = tema.fondo
        )
    }

    if (showLeaveGroupDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveGroupDialog = false },
            title = { Text("Salir del Grupo", color = tema.letras) },
            text = { Text("¿Estás seguro de que deseas salir del grupo \"${grupo.nombreGrupo}\"?", color = tema.letras) },
            confirmButton = {
                Button(
                    onClick = {
                        showLeaveGroupDialog = false
                        onLeaveGroupClick?.invoke(grupo)
                        onBackClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = Color.White)
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveGroupDialog = false }) {
                    Text("Cancelar", color = tema.letras)
                }
            },
            containerColor = tema.fondo
        )
    }

    if (showOwnerLeaveWarningDialog) {
        AlertDialog(
            onDismissRequest = { showOwnerLeaveWarningDialog = false },
            title = { Text("No puedes salir del grupo", color = tema.letras) },
            text = { Text("Eres el propietario del grupo. Primero debes transferir la propiedad a otro integrante o eliminar el grupo.", color = tema.letras) },
            confirmButton = {
                Button(
                    onClick = { showOwnerLeaveWarningDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = tema.botones, contentColor = tema.fondo)
                ) {
                    Text("Aceptar")
                }
            },
            containerColor = tema.fondo
        )
    }

    if (showAiDialog) {
        AlertDialog(
            onDismissRequest = { showAiDialog = false },
            title = { Text("Agendando Actividad Grupal con IA", color = tema.letras) },
            text = { Text("Gemini está analizando la solicitud y las agendas de los miembros para programar esta actividad en conjunto sin conflictos.", color = tema.letras) },
            confirmButton = {
                Button(onClick = { showAiDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = tema.botones)) {
                    Text("Aceptar", color = tema.fondo)
                }
            },
            containerColor = tema.fondo
        )
    }

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = grupo.nombreGrupo,
                tema = tema,
                onBackClick = onBackClick
            )
        },
        containerColor = tema.fondo
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (grupo.descripcion.isNotBlank()) {
                    Text(text = "Descripción", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = tema.letras)
                    Text(text = grupo.descripcion, color = tema.letras.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(text = "Código de Invitación", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = tema.letras)
                val codigoParaMostrar = if (grupo.codigoInvitacion.isNotBlank()) grupo.codigoInvitacion else "GRP${if (grupo.id > 0) grupo.id else 101}920"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = codigoParaMostrar, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = tema.botones, modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(codigoParaMostrar))
                        copied = true
                    }) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copiar", tint = tema.botones)
                    }
                }
                if (copied) {
                    Text("¡Copiado al portapapeles!", color = tema.detalles, fontSize = 12.sp)
                }
            }

            item {
                // Sección de Agendar Actividad Grupal con IA (Gemini)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = tema.botones, modifier = Modifier.size(20.dp))
                            Text(text = "Agendar Actividad con IA (Gemini)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = tema.letras)
                        }
                        OutlinedTextField(
                            value = aiPrompt,
                            onValueChange = { aiPrompt = it },
                            label = { Text("Ej. Reunión de equipo el 3 de noviembre", color = tema.letras) },
                            textStyle = TextStyle(color = Color.Black),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedLabelColor = tema.letras,
                                unfocusedLabelColor = tema.letras.copy(alpha = 0.7f),
                                focusedBorderColor = tema.botones,
                                unfocusedBorderColor = tema.detalles
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        Button(
                            onClick = {
                                if (aiPrompt.isNotBlank()) {
                                    onAiScheduleClick(aiPrompt)
                                    showAiDialog = true
                                    aiPrompt = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = tema.botones, contentColor = tema.fondo)
                        ) {
                            Text("Analizar y Agendar en Grupo")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Integrantes (${integrantesList.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = tema.letras
                )
            }

            items(integrantesList) { miembro ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = miembro.nickname,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = tema.letras,
                            modifier = Modifier.weight(1f)
                        )
                        if (miembro.id == grupo.propietario.id || miembro.email == grupo.propietario.email) {
                            Surface(
                                color = tema.botones,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    "Admin",
                                    color = tema.fondo,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                if (actividades.isNotEmpty()) {
                    Text(
                        text = "Actividades del Grupo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = tema.letras,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            items(actividades) { actividad ->
                val inicioTime = try { actividad.segmentoTiempo.inicio.toLocalTime().toString().take(5) } catch (_: Exception) { "" }
                val finTime = try { actividad.segmentoTiempo.fin.toLocalTime().toString().take(5) } catch (_: Exception) { "" }
                val segmentoString = if (inicioTime.isNotEmpty() && finTime.isNotEmpty()) "$inicioTime - $finTime" else "Horario no especificado"
                val creadorNickname = actividad.propietario.nickname

                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onEditActivityClick?.invoke(actividad) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3B82F6).copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = actividad.titulo, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = tema.letras)
                        if (actividad.descripcion.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = actividad.descripcion, fontSize = 14.sp, color = tema.letras.copy(alpha = 0.8f))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Creada por: $creadorNickname", fontSize = 13.sp, color = tema.letras.copy(alpha = 0.7f))
                            Text(text = segmentoString, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Botón para salir o eliminar
                if (isOwner) {
                    Button(
                        onClick = { showDeleteGroupDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = Color.White)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar Grupo")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Eliminar Grupo")
                    }
                } else {
                    Button(
                        onClick = { showLeaveGroupDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = Color.White)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Salir del Grupo")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Salir del Grupo")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupDetailScreenPreview() {
    val sampleUser = Usuario(id = 1, nickname = "Josue", email = "josue@example.com")
    val sampleGroup = Grupo(
        id = 1,
        nombreGrupo = "Estudio Kotlin",
        descripcion = "Grupo de práctica y desarrollo",
        propietario = sampleUser,
        codigoInvitacion = "GRP123456",
        miebros = listOf(sampleUser)
    )
    GroupDetailScreen(
        tema = obtenerTema(1),
        grupoId = 1,
        grupos = listOf(sampleGroup),
        currentUserId = "josue@example.com",
        onBackClick = {},
        onNavigateToCalendar = {}
    )
}
