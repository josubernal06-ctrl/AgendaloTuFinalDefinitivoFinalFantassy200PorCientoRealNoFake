package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNavigationExample(
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Navigation example",
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Text(
            "Click the back button to pop from the back stack.",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiTopBar(
    titulo: String,
    tema: TemaColor,
    onBackClick: (() -> Unit)? = null, // Opcional: si es null no muestra la flecha de regresar
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    var textoIngresado by remember { mutableStateOf("") }
    var buscadorVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = tema.botones)
            .padding(bottom = if (buscadorVisible) 8.dp else 0.dp)
    ) {
        TopAppBar(
            title = { Text(text = titulo, color = tema.letras, fontStyle = FontStyle.Normal) },
            navigationIcon = {
                if (onBackClick != null) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = tema.letras
                        )
                    }
                }
            },
            actions = {
                Row(
                    modifier = Modifier.padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Botón con icono de Lupa (Search) para desplegar/ocultar el buscador
                    IconButton(onClick = {
                        buscadorVisible = !buscadorVisible
                        if (!buscadorVisible) {
                            textoIngresado = "" // Limpia la búsqueda al cerrar
                        }
                    }) {
                        Icon(
                            imageVector = if (buscadorVisible) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (buscadorVisible) "Cerrar búsqueda" else "Buscar",
                            tint = tema.letras
                        )
                    }

                    IconButton(onClick = { /* Configuración */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración",
                            tint = tema.letras
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = tema.botones,
                scrolledContainerColor = tema.botones
            ),
            scrollBehavior = scrollBehavior
        )

        // Se despliega la barra de búsqueda únicamente al presionar el icono de la lupa
        AnimatedVisibility(visible = buscadorVisible) {
            EntradaTextoField(
                valor = textoIngresado,
                onValueChange = { textoIngresado = it },
                label = "Buscar...",
                tema = tema,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
