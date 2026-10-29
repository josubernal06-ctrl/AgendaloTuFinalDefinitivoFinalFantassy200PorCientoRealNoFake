package com.example.agendalotufinaldefinitivofinalfantassy200realnofake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di.AppContainer
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.CreateActivityScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.ActivityScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.AlarmScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.CreateGroupScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.GroupDetailScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.GroupListScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.HomeScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.JoinGroupScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.UserProfileScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.ActividadesViewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.GrupoUiState
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.GrupoViewModel

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

                    composable("groups") {
                        val grupoViewModel: GrupoViewModel = viewModel(factory = grupoFactory)
                        val grupoUiState by grupoViewModel.uiState.collectAsStateWithLifecycle()

                        androidx.compose.runtime.LaunchedEffect(Unit) {
                            grupoViewModel.cargarGrupos(usuarioActual.id)
                        }

                        GroupListScreen(
                            tema = temaActual,
                            uiState = grupoUiState,
                            onNavigateToHome = {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                    launchSingleTop = true
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
                        val grupoViewModel: GrupoViewModel = viewModel(factory = grupoFactory)
                        CreateGroupScreen(
                            tema = temaActual,
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { nombre, descripcion ->
                                grupoViewModel.crearGrupo(nombre, descripcion, usuarioActual)
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("join_group") {
                        val grupoViewModel: GrupoViewModel = viewModel(factory = grupoFactory)
                        JoinGroupScreen(
                            tema = temaActual,
                            onBackClick = { navController.popBackStack() },
                            onJoinClick = { codigo ->
                                grupoViewModel.unirseAGrupo(codigo, usuarioActual)
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("group_detail/{grupoId}") { backStackEntry ->
                        val grupoViewModel: GrupoViewModel = viewModel(factory = grupoFactory)
                        val grupoUiState by grupoViewModel.uiState.collectAsStateWithLifecycle()
                        val grupoId = backStackEntry.arguments?.getString("grupoId")?.toIntOrNull() ?: 0
                        val gruposList = (grupoUiState as? GrupoUiState.Success)?.grupos ?: emptyList()

                        GroupDetailScreen(
                            tema = temaActual,
                            grupoId = grupoId,
                            grupos = gruposList,
                            onBackClick = { navController.popBackStack() },
                            onNavigateToCalendar = { id ->
                                navController.navigate("activity")
                            }
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
