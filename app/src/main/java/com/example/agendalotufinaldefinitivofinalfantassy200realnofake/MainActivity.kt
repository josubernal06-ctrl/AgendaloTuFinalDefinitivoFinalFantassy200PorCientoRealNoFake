package com.example.agendalotufinaldefinitivofinalfantassy200realnofake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme

/*KENDRA TE AMO MUCHO CARAJO*/
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var presses by remember { mutableIntStateOf(0) }
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val searchState = remember { TextFieldState() }
            var results by remember { mutableStateOf(listOf<String>()) }
            val tabTitles = listOf("Calendar", "Alarm")
            val tabIcons = listOf(
                Icons.Default.CalendarMonth,
                Icons.Default.Alarm,
            )
            AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppTopBar()
                    },
                    bottomBar = {
                        TabRow(
                            selectedTabIndex = selectedTabIndex,
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            tabTitles.forEachIndexed {index, title ->
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
                            verticalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre los dos botones
                            horizontalAlignment = Alignment.End              // Alineados a la derecha
                        ) {
                            // Primer Botón Flotante
                            ExtendedExample("Actividad Simple", onClick = {
                                // Acción del botón 1
                            })

                            // Segundo Botón Flotante
                            ExtendedExample("Actividad en Conjunto", onClick = {
                                // Acción del botón 2
                            })
                        }
                    },

                ) { innerPadding ->
                    CalendarGridView(innerPadding)

                }
             }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
        }
    }
}