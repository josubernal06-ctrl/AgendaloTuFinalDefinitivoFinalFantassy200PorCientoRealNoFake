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
fun JoinGroupScreen(
    tema: TemaColor,
    onBackClick: () -> Unit,
    onJoinClick: (String) -> Unit
) {
    var codigo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            MiTopBar(
                titulo = "Unirse a Grupo",
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
            Text(
                text = "Pídele al administrador del grupo el código de invitación e introdúcelo aquí.",
                color = tema.letras,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            EntradaTextoField(
                valor = codigo,
                onValueChange = { codigo = it.uppercase() },
                label = "Código de Invitación *",
                tema = tema,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            BotonPersonalizado(
                texto = "Unirse al Grupo",
                onClick = {
                    if (codigo.isNotBlank()) {
                        onJoinClick(codigo)
                    }
                },
                colorFondo = tema.botones,
                colorTexto = tema.fondo
            )
        }
    }
}
