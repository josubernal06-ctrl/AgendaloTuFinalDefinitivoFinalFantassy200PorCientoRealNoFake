package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
    searchQuery: String? = null,
    onSearchQueryChange: ((String) -> Unit)? = null,
    searchLabel: String = "Buscar...",
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    var internalQuery by remember { mutableStateOf("") }
    val currentQuery = searchQuery ?: internalQuery
    val onQueryChange: (String) -> Unit = { newQuery ->
        internalQuery = newQuery
        onSearchQueryChange?.invoke(newQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = tema.botones)
            .padding(bottom = 12.dp)
    ) {
        MediumTopAppBar(
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
                    modifier = Modifier.padding(end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { /* Configuración */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración",
                            tint = tema.letras
                        )
                    }
                    if (currentQuery.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.ClearAll,
                                contentDescription = "Limpiar búsqueda",
                                tint = tema.letras
                            )
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = tema.botones,
                scrolledContainerColor = tema.botones
            ),
            scrollBehavior = scrollBehavior
        )

        // Barra de búsqueda siempre visible integrada al tema
        EntradaTextoField(
            valor = currentQuery,
            onValueChange = onQueryChange,
            label = searchLabel,
            tema = tema,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
