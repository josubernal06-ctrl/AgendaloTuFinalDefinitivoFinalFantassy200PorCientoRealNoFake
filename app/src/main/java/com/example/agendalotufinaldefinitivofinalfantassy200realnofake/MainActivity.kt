package com.example.agendalotufinaldefinitivofinalfantassy200realnofake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.model.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.HomeScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.UserProfileScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
                // Instanciamos un usuario falso para mostrar la pantalla
                val usuarioActual = Usuario(id = 101, nombre = "María García", email = "maria.garcia@gmail.com")
                val temaActual = obtenerTema(1) // Usamos el tema 1 como ejemplo

                // Estado simple para la navegación
                var showProfile by remember { mutableStateOf(false) }

                if (showProfile) {
                    UserProfileScreen(
                        usuario = usuarioActual,
                        tema = temaActual,
                        onBackClick = { showProfile = false }
                    )
                } else {
                    HomeScreen(
                        tema = temaActual,
                    ) { showProfile = true }
                }
            }
        }
    }
}
