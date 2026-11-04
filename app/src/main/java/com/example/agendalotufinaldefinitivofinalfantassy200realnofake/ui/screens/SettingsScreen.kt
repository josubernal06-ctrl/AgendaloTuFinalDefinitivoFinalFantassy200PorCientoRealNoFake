package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    tema: TemaColor,
    selectedThemeId: Int,
    onThemeSelected: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val textColor = tema.letras

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Tema", color = tema.fondo, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = tema.fondo
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = tema.botones)
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Selección de Tema de Color
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = null,
                        tint = tema.botones,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Tema de la Aplicación",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }

                Text(
                    text = "Selecciona uno de los 13 temas de color disponibles:",
                    fontSize = 13.sp,
                    color = textColor.copy(alpha = 0.7f)
                )

                // Grid de los 13 temas
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    (1..13).chunked(3).forEach { rowThemeIds ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowThemeIds.forEach { themeId ->
                                val themeObj = remember(themeId) { obtenerTema(themeId) }
                                val isSelected = themeId == selectedThemeId

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onThemeSelected(themeId) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) tema.botones.copy(alpha = 0.2f) else tema.detalles.copy(alpha = 0.12f)
                                    ),
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, tema.botones) else null
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(themeObj.fondo))
                                            Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(themeObj.botones))
                                            Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(themeObj.detalles))
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "Tema $themeId",
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = textColor
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Seleccionado",
                                                    tint = tema.botones,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            if (rowThemeIds.size < 3) {
                                repeat(3 - rowThemeIds.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        tema = obtenerTema(1),
        selectedThemeId = 1,
        onThemeSelected = {},
        onBackClick = {}
    )
}