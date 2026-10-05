package com.example.agendalotufinaldefinitivofinalfantassy200realnofake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.model.Usuario
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

                UserProfileScreen(
                    usuario = usuarioActual,
                    tema = temaActual,
                    onBackClick = { /* TODO: Navegación hacia atrás */ }
                )
            }
        }
    }
}
