package com.example.agendalotufinaldefinitivofinalfantassy200realnofake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di.AppContainer
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.ActividadConjunta
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.SegmentoTiempo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.viewmodel.AiViewModel
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
                val usuarioActual = Usuario(id = 101, nickname = "María García", email = "maria.garcia@gmail.com")
                val temaActual = obtenerTema(13)

                val context = LocalContext.current
                val appContainer = remember { AppContainer(context) }
                val factory = ActividadesViewModel.Factory(appContainer.actividadRepository)
                val grupoFactory = GrupoViewModel.Factory(appContainer.grupoRepository)
                val grupoViewModel: GrupoViewModel = viewModel(factory = grupoFactory)
                val aiFactory = AiViewModel.Factory(appContainer.aiRepository)
                val aiViewModel: AiViewModel = viewModel(factory = aiFactory)
                val authFactory = AuthViewModel.Factory(appContainer.authRepository)
                val authViewModel: AuthViewModel = viewModel(factory = authFactory)
                val currentUserState by authViewModel.currentUser.collectAsStateWithLifecycle()
                val usuarioActivo = currentUserState ?: usuarioActual

                val navController = rememberNavController()

                // Cargar grupos automáticamente para el usuario actual
                LaunchedEffect(usuarioActual) {
                    grupoViewModel.cargarGrupos(usuarioActual.id)
                }

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
                            onNavigateToGroups = {
                                navController.navigate("groups") {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onNavigateToActivity = { navController.navigate("activity") }
                        )
                    }

                    composable("groups") {
                        val uiState by grupoViewModel.uiState.collectAsStateWithLifecycle()
                        GroupListScreen(
                            tema = temaActual,
                            uiState = uiState,
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onNavigateToAlarm = {
                                navController.navigate("alarm") {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onCreateGroup = { navController.navigate("create_group") },
                            onJoinGroup = { navController.navigate("join_group") },
                            onGroupClick = { grupo -> navController.navigate("group_detail/${grupo.id}") }
                        )
                    }

                    composable("create_group") {
                        val actionResult by grupoViewModel.actionResult.collectAsStateWithLifecycle()
                        LaunchedEffect(actionResult) {
                            if (actionResult is Resultado.Exito) {
                                grupoViewModel.clearActionResult()
                                navController.popBackStack()
                            }
                        }
                        CreateGroupScreen(
                            tema = temaActual,
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { nombre, desc ->
                                grupoViewModel.crearGrupo(nombre, desc, usuarioActual)
                            }
                        )
                    }

                    composable("join_group") {
                        val actionResult by grupoViewModel.actionResult.collectAsStateWithLifecycle()
                        LaunchedEffect(actionResult) {
                            if (actionResult is Resultado.Exito) {
                                grupoViewModel.clearActionResult()
                                navController.popBackStack()
                            }
                        }
                        JoinGroupScreen(
                            tema = temaActual,
                            onBackClick = { navController.popBackStack() },
                            onJoinClick = { codigo ->
                                grupoViewModel.unirseAGrupo(codigo, usuarioActual)
                            }
                        )
                    }

                    composable(
                        "group_detail/{grupoId}",
                        arguments = listOf(navArgument("grupoId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val grupoId = backStackEntry.arguments?.getInt("grupoId") ?: -1
                        val uiState by grupoViewModel.uiState.collectAsStateWithLifecycle()
                        val grupos = (uiState as? GrupoUiState.Success)?.grupos ?: emptyList()

                        GroupDetailScreen(
                            tema = temaActual,
                            grupoId = grupoId,
                            grupos = grupos,
                            onBackClick = { navController.popBackStack() },
                            onNavigateToCalendar = { id -> navController.navigate("group_calendar/$id") }
                        )
                    }

                    composable(
                        "group_calendar/{grupoId}",
                        arguments = listOf(navArgument("grupoId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val grupoId = backStackEntry.arguments?.getInt("grupoId") ?: -1
                        val uiState by grupoViewModel.uiState.collectAsStateWithLifecycle()
                        val grupo = (uiState as? GrupoUiState.Success)?.grupos?.find { it.id == grupoId }

                        val actViewModel: ActividadesViewModel = viewModel(factory = factory)
                        val actUiState by actViewModel.uiState.collectAsStateWithLifecycle()
                        val actividades = actUiState.actividades.filterIsInstance<ActividadConjunta>()

                        if (grupo != null) {
                            GroupActivityCalendarScreen(
                                tema = temaActual,
                                grupo = grupo,
                                actividades = actividades,
                                onBackClick = { navController.popBackStack() },
                                onNavigateToCreateActivity = { id -> navController.navigate("create_group_activity/$id") }
                            )
                        }
                    }

                    composable(
                        "create_group_activity/{grupoId}",
                        arguments = listOf(navArgument("grupoId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val grupoId = backStackEntry.arguments?.getInt("grupoId") ?: -1
                        val actViewModel: ActividadesViewModel = viewModel(factory = factory)
                        val aiState by aiViewModel.responseText.collectAsStateWithLifecycle()

                        CreateGroupActivityScreen(
                            tema = temaActual,
                            grupoId = grupoId,
                            usuarioActual = usuarioActual,
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { titulo, desc, rec ->
                                val nuevaActividad = ActividadConjunta(
                                    id = (1000..9999).random(),
                                    titulo = titulo,
                                    descripcion = desc + "\n[Recomendación AI: $rec]",
                                    propietario = usuarioActual,
                                    esPrivada = false,
                                    segmentoTiempo = SegmentoTiempo(
                                        inicio = LocalDateTime.now().plusDays(1).withHour(15).withMinute(0),
                                        fin = LocalDateTime.now().plusDays(1).withHour(16).withMinute(0),
                                        esConjunta = true,
                                        puntuacion = 1.0
                                    ),
                                    grupoId = grupoId,
                                    participantes = emptyList()
                                )
                                actViewModel.crearActividad(nuevaActividad)
                                navController.popBackStack()
                            },
                            aiRecommendationState = aiState,
                            onSuggestClick = { prompt ->
                                aiViewModel.onPromptChanged(prompt)
                                aiViewModel.enviarPromptAGemini()
                            }
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
                            onNavigateToGroups = {
                                navController.navigate("groups") {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            onNavigateToProfile = { navController.navigate("profile") }
                        )
                    }

                    composable("profile") {
                        UserProfileScreen(
                            usuario = usuarioActivo,
                            tema = temaActual,
                            onBackClick = { navController.popBackStack() },
                            onLogoutClick = { authViewModel.logout() }
                        )
                    }
                }
            }
        }
    }
}
