package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiTopBar(
    titulo: String,
    tema: TemaColor,
    onBackClick: (() -> Unit)? = null,      // si es null no muestra la flecha de regresar
    onPerfilClick: (() -> Unit)? = null,    // si es null no muestra el ícono de perfil
    onSettingsClick: () -> Unit = {},
    searchQuery: String? = null,
    onSearchQueryChange: ((String) -> Unit)? = null,
    searchLabel: String = "Buscar...",
    showSearchBar: Boolean = false,
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
    ) {
        TopAppBar(
            title = {
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                if (onBackClick != null) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            },
            actions = {
                if (showSearchBar && currentQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.ClearAll,
                            contentDescription = "Limpiar búsqueda"
                        )
                    }
                }
                if (onPerfilClick != null) {
                    IconButton(onClick = onPerfilClick) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Tu perfil"
                        )
                    }
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Configuración"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = tema.botones,
                scrolledContainerColor = tema.botones,
                titleContentColor = tema.fondo,
                navigationIconContentColor = tema.fondo,
                actionIconContentColor = tema.fondo
            ),
            scrollBehavior = scrollBehavior
        )

        if (showSearchBar) {
            EntradaTextoField(
                valor = currentQuery,
                onValueChange = onQueryChange,
                label = searchLabel,
                tema = tema,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            )
        }
    }
}
