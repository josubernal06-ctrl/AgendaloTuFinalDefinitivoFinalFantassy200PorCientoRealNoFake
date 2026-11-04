package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.BotonPersonalizado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.EntradaTextoField
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.MiTopBar
import androidx.compose.ui.tooling.preview.Preview
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGroupScreen(
    tema: TemaColor,
    actionResult: Resultado<String>? = null,
    onBackClick: () -> Unit,
    onJoinClick: (String) -> Unit
) {
    var codigo by remember { mutableStateOf("") }
    val isLoading = actionResult is Resultado.Cargando
    val errorMsg = (actionResult as? Resultado.Error)?.excepcion?.message

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
                .imePadding()
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

            if (errorMsg != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (isLoading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = tema.botones)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            BotonPersonalizado(
                texto = if (isLoading) "Uniéndose..." else "Unirse al Grupo",
                onClick = {
                    if (codigo.isNotBlank() && !isLoading) {
                        onJoinClick(codigo)
                    }
                },
                colorFondo = tema.botones,
                colorTexto = tema.fondo
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinGroupScreenPreview() {
    JoinGroupScreen(
        tema = obtenerTema(1),
        onBackClick = {},
        onJoinClick = {}
    )
}
