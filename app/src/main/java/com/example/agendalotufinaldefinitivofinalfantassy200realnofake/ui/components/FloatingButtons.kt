package com.example.isnotempty.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Example(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorFondo: Color,
    colorTexto: Color
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = colorFondo,
        contentColor = colorTexto
    ) {
        Icon(Icons.Default.Call, "Floating action button.")
    }
}