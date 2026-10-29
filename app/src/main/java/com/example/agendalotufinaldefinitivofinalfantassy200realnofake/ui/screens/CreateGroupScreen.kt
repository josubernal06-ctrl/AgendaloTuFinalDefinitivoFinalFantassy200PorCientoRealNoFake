package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.BotonPersonalizado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.EntradaTextoField
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    tema: TemaColor,
    onBackClick: () -> Unit,
    onSaveClick: (String, String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Crear Grupo",
                tema = tema,
                onBackClick = onBackClick
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
            EntradaTextoField(
                valor = nombre,
                onValueChange = { nombre = it },
                label = "Nombre del Grupo *",
                tema = tema,
                modifier = Modifier.fillMaxWidth()
            )
            
            EntradaTextoField(
                valor = descripcion,
                onValueChange = { descripcion = it },
                label = "Descripción (Opcional)",
                tema = tema,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            BotonPersonalizado(
                texto = "Crear Grupo",
                onClick = {
                    if (nombre.isNotBlank()) {
                        onSaveClick(nombre, descripcion)
                    }
                },
                colorFondo = tema.botones,
                colorTexto = tema.fondo
            )
        }
    }
}
