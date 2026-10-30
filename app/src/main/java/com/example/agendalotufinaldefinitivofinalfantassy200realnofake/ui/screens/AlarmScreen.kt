package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
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
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.alarm.AlarmScheduler
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    tema: TemaColor,
    onNavigateToHome: () -> Unit,
    onNavigateToGroups: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context) }
    val scheduler = remember { AlarmScheduler(context) }

    var alarmasState by remember { mutableStateOf<List<AlarmEntity>>(emptyList()) }

    // Cargar las alarmas desde Room
    androidx.compose.runtime.LaunchedEffect(Unit) {
        database.alarmDao().obtenerTodasLasAlarmas().collect { lista ->
            alarmasState = lista
        }
    }

    // Solicitud de permiso de notificaciones para Android 13+
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    var selectedTabIndex by remember { mutableIntStateOf(2) } // 2 = Alarm
    val tabTitles = listOf("Home", "Grupos", "Alarma")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Group,
        Icons.Default.Alarm
    )

    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTimeText by remember { mutableStateOf("08:00 AM") }
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Alarmas",
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
                            if (index == 0) {
                                onNavigateToHome()
                            } else if (index == 1) {
                                onNavigateToGroups()
                            }
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
                            androidx.compose.material3.Surface(
                                color = if (isSelected) androidx.compose.ui.graphics.Color.White.copy(alpha = 0.25f) else androidx.compose.ui.graphics.Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                androidx.compose.foundation.layout.Box(
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Gestión de Alarmas",
                color = tema.letras,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Botón para agregar o modificar la hora
            Button(
                onClick = { showTimePicker = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = tema.botones,
                    contentColor = tema.fondo
                )
            ) {
                Text(
                    text = "Configurar Nueva Alarma",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = tema.fondo
                )
            }

            Text(
                text = "Alarmas Programadas (${alarmasState.size}):",
                color = tema.letras,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (alarmasState.isEmpty()) {
                Text(
                    text = "No tienes alarmas configuradas.",
                    color = tema.letras.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            } else {
                alarmasState.forEach { alarma ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.12f))
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
                                    text = alarma.tituloActividad,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = tema.letras
                                )
                                Text(
                                    text = "Programada: ${alarma.fechaHoraAlarma.replace("T", " ")}",
                                    fontSize = 12.sp,
                                    color = tema.letras.copy(alpha = 0.8f)
                                )
                            }
                            IconButton(
                                onClick = {
                                    coroutineScope.launch(Dispatchers.IO) {
                                        database.alarmDao().eliminarAlarma(alarma)
                                        scheduler.cancelarAlarma(alarma.id)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar Alarma",
                                    tint = tema.letras
                                )
                            }
                        }
                    }
                }
            }

            // Diálogo de selector de hora temático
            if (showTimePicker) {
                ThemedTimePickerDialog(
                    tema = tema,
                    onDismiss = { showTimePicker = false },
                    onConfirm = { timePickerState ->
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        cal.set(Calendar.MINUTE, timePickerState.minute)
                        cal.set(Calendar.SECOND, 0)

                        if (cal.before(Calendar.getInstance())) {
                            cal.add(Calendar.DAY_OF_YEAR, 1) // Si ya pasó hoy, programar para mañana
                        }

                        selectedTimeText = formatter.format(cal.time)

                        val localDateTime = LocalDateTime.of(
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.DAY_OF_MONTH),
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE)
                        )

                        coroutineScope.launch(Dispatchers.IO) {
                            val entity = AlarmEntity(
                                actividadId = 0,
                                tituloActividad = "Alarma Personal (${formatter.format(cal.time)})",
                                fechaHoraAlarma = localDateTime.toString(),
                                estaActiva = true
                            )
                            val newId = database.alarmDao().insertarAlarma(entity).toInt()
                            scheduler.programarAlarma(newId, entity.tituloActividad, localDateTime)
                        }

                        showTimePicker = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedTimePickerDialog(
    tema: TemaColor,
    onDismiss: () -> Unit,
    onConfirm: (TimePickerState) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState) }) {
                Text("Aceptar", color = tema.botones, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = tema.letras)
            }
        },
        title = {
            Text(
                text = "Seleccionar Hora",
                color = tema.letras,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = tema.detalles.copy(alpha = 0.2f),
                    selectorColor = tema.botones,
                    containerColor = tema.fondo,
                    periodSelectorBorderColor = tema.botones,
                    periodSelectorSelectedContainerColor = tema.botones,
                    periodSelectorUnselectedContainerColor = tema.detalles.copy(alpha = 0.1f),
                    periodSelectorSelectedContentColor = tema.fondo,
                    periodSelectorUnselectedContentColor = tema.letras,
                    timeSelectorSelectedContainerColor = tema.botones,
                    timeSelectorUnselectedContainerColor = tema.detalles.copy(alpha = 0.15f),
                    timeSelectorSelectedContentColor = tema.fondo,
                    timeSelectorUnselectedContentColor = tema.letras
                )
            )
        },
        containerColor = tema.fondo
    )
}
