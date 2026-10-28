package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@Composable
fun LoginScreen(
    tema: TemaColor,
    testUsers: List<Usuario>,
    onLoginClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("AgendaloTu", style = MaterialTheme.typography.headlineLarge, color = tema.letras)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Iniciar sesión (Mock)", style = MaterialTheme.typography.bodyLarge, color = tema.letras.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(32.dp))

        if (testUsers.isEmpty()) {
            CircularProgressIndicator(color = tema.botones)
        } else {
            testUsers.forEach { user ->
                Button(
                    onClick = { onLoginClick(user.email) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = tema.botones, contentColor = tema.fondo)
                ) {
                    Text("Ingresar como ${user.nickname}")
                }
            }
        }
    }
}
