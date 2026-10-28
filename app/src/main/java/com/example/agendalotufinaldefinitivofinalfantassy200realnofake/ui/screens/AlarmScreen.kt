package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
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
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.Example
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    tema: TemaColor,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(1) } // 1 = Alarm
    val tabTitles = listOf("Calendar", "Alarm")
    val tabIcons = listOf(
        Icons.Default.CalendarMonth,
        Icons.Default.Alarm,
    )

    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTimeText by remember { mutableStateOf("08:00 AM") }
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Alarmas",
                tema = tema,
                onBackClick = null
            )
        },
        bottomBar = {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.navigationBarsPadding(),
                containerColor = tema.botones,
                contentColor = tema.fondo,
                indicator = { tabPositions ->
                    if (selectedTabIndex < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = tema.fondo
                        )
                    }
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            if (index == 0) {
                                onNavigateToHome()
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        selectedContentColor = tema.fondo,
                        unselectedContentColor = tema.letras.copy(alpha = 0.6f)
                    )
                }
            }
        },
        floatingActionButton = {
            Example(
                onClick = { /*not yet*/ },
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

            // Tarjeta con la alarma configurada
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Próxima Alarma",
                        fontSize = 14.sp,
                        color = tema.letras.copy(alpha = 0.7f)
                    )
                    Text(
                        text = selectedTimeText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = tema.botones
                    )
                    Text(
                        text = "Programada para tus actividades agendadas",
                        fontSize = 14.sp,
                        color = tema.letras
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para agregar o modificar la hora
            Button(
                onClick = { /*no aun, mucha complejidad*/ },
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

            // Diálogo de selector de hora temático
            if (showTimePicker) {
                ThemedTimePickerDialog(
                    tema = tema,
                    onDismiss = { showTimePicker = false },
                    onConfirm = { timePickerState ->
                        val cal = Calendar.getInstance()
                        cal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                        cal.set(Calendar.MINUTE, timePickerState.minute)
                        selectedTimeText = formatter.format(cal.time)
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
