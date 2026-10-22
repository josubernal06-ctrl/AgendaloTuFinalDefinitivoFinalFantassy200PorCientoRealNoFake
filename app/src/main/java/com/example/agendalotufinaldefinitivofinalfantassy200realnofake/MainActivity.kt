package com.example.agendalotufinaldefinitivofinalfantassy200realnofake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.AppDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.ActividadSolitaria
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.SegmentoTiempo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.CreateActivityScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.ActivityScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.AlarmScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.HomeScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.UserProfileScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.ActividadesViewModel
import java.time.LocalDateTime

class  MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
                val usuarioActual = Usuario(id = 101, nickname = "María García", email = "maria.garcia@gmail.com")
                val temaActual = obtenerTema(1)

                val context = LocalContext.current
                val database = AppDatabase.getDatabase(context)
                val repository = ActividadRepository(database.actividadDao())
                val factory = ActividadesViewModel.Factory(repository)

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            tema = temaActual,
                            onNavigateToProfile = { navController.navigate("profile") },
                            onNavigateToAlarm = {
                                navController.navigate("alarm") {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onNavigateToActivity = { navController.navigate("activity") }
                        )
                    }

                    composable("activity") {
                        val viewModel: ActividadesViewModel = viewModel(factory = factory)
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        
                        ActivityScreen(
                            tema = temaActual,
                            uiState = uiState,
                            onBackClick = { navController.popBackStack() },
                            onNavigateToCreate = { navController.navigate("create_activity") },
                            onDeleteActivity = { viewModel.eliminarActividad(it) }
                        )
                    }

                    composable("create_activity") {
                        val viewModel: ActividadesViewModel = viewModel(factory = factory)
                        
                        CreateActivityScreen(
                            tema = temaActual,
                            usuarioActual = usuarioActual,
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { nuevaActividad ->
                                viewModel.crearActividad(nuevaActividad)
                            }
                        )
                    }

                    composable("alarm") {
                        AlarmScreen(
                            tema = temaActual,
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            onNavigateToProfile = { navController.navigate("profile") }
                        )
                    }

                    composable("profile") {
                        UserProfileScreen(
                            usuario = usuarioActual,
                            tema = temaActual,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
