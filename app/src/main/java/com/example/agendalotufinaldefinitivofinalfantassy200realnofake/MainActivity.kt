package com.example.agendalotufinaldefinitivofinalfantassy200realnofake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.AbstractActividad
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.SegmentoTiempo
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.CreateActivityScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.ActivityScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.AlarmScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.HomeScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.UserProfileScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di.AppContainer
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.*
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.AiViewModel
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.ActividadConjunta
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.ActividadRepository
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.time.LocalDateTime

//quiero anal
class  MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgendaloTuFinalDefinitivoFinalFantassy200RealNoFakeTheme {
                val context = LocalContext.current
                val appContainer = remember { AppContainer(context) }
                
                val authViewModel: AuthViewModel = viewModel()
                val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
                
                val actividadFactory = ActividadesViewModel.Factory(appContainer.actividadRepository)
                val grupoFactory = GrupoViewModel.Factory(appContainer.grupoRepository)
                val grupoViewModel: GrupoViewModel = viewModel(factory = grupoFactory)
                val aiViewModel: AiViewModel = viewModel()

                val navController = rememberNavController()
                val themePreferences = remember { appContainer.themePreferences }
                val userPreferences = remember { appContainer.userPreferences }
                var temaId by remember { mutableIntStateOf(themePreferences.getSelectedThemeId()) }
                val temaActual = obtenerTema(temaId)
                
                var showSplash by remember { mutableStateOf(true) }
                
                // Cargar grupos y aplicar nickname guardado cuando el usuario inicie sesión
                LaunchedEffect(currentUser) {
                    if (currentUser != null) {
                        val savedNick = userPreferences.getNickname(currentUser!!.email, currentUser!!.nickname)
                        if (savedNick.isNotBlank()) {
                            currentUser!!.nickname = savedNick
                        }
                        grupoViewModel.init(currentUser!!.id)
                    }
                }

                if (showSplash) {
                    SplashScreen(
                        tema = temaActual,
                        onSplashFinished = { showSplash = false }
                    )
                } else if (currentUser == null) {
                    LoginScreen(tema = temaActual, viewModel = authViewModel)
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                    composable("home") {
                        val actividadesViewModel: ActividadesViewModel = viewModel(factory = actividadFactory)
                        HomeScreen(
                            tema = temaActual,
                            grupoViewModel = grupoViewModel,
                            actividadesViewModel = actividadesViewModel,
                            onNavigateToProfile = { navController.navigate("profile") },
                            onNavigateToSettings = { navController.navigate("settings") },
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
                            onNavigateToSettings = { navController.navigate("settings") },
                            onCreateGroup = { navController.navigate("create_group") },
                            onJoinGroup = { navController.navigate("join_group") },
                            onGroupClick = { grupo -> navController.navigate("group_detail/${grupo.id}") },
                            onDeleteGroupClick = { grupo -> grupoViewModel.eliminarGrupo(grupo, currentUser!!.id) }
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
                                grupoViewModel.crearGrupo(nombre, desc, currentUser!!) 
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
                            actionResult = actionResult,
                            onBackClick = {
                                grupoViewModel.clearActionResult()
                                navController.popBackStack()
                            },
                            onJoinClick = { codigo -> 
                                grupoViewModel.unirseAGrupo(codigo, currentUser!!) 
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
                        
                        val actividadViewModel: ActividadesViewModel = viewModel(factory = actividadFactory)
                        val actUiState by actividadViewModel.uiState.collectAsStateWithLifecycle()
                        val actividades = actUiState.actividades.filterIsInstance<ActividadConjunta>().filter { it.grupoId == grupoId }
                        
                        GroupDetailScreen(
                            tema = temaActual,
                            grupoId = grupoId,
                            grupos = grupos,
                            actividades = actividades,
                            currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: currentUser!!.email,
                            onBackClick = { navController.popBackStack() },
                            onNavigateToCalendar = { id -> navController.navigate("group_calendar/$id") },
                            onDeleteGroupClick = { grupo -> grupoViewModel.eliminarGrupo(grupo, currentUser!!.id) },
                            onLeaveGroupClick = { grupo -> grupoViewModel.salirDelGrupo(grupo, currentUser!!.id) },
                            onEditActivityClick = { _ -> },
                            onAiScheduleClick = { prompt ->
                                val grupo = grupos.find { it.id == grupoId }
                                aiViewModel.onPromptChanged("Para el grupo ${grupo?.nombreGrupo ?: grupoId}: $prompt")
                                aiViewModel.enviarPromptAGemini(
                                    grupoId = grupoId,
                                    context = context,
                                    actividadRepository = appContainer.actividadRepository as ActividadRepository,
                                    usuario = currentUser
                                )
                            }
                        )
                    }
                    
                    composable(
                        "group_calendar/{grupoId}",
                        arguments = listOf(navArgument("grupoId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val grupoId = backStackEntry.arguments?.getInt("grupoId") ?: -1
                        val uiState by grupoViewModel.uiState.collectAsStateWithLifecycle()
                        val grupo = (uiState as? GrupoUiState.Success)?.grupos?.find { it.id == grupoId }
                        
                        val actividadViewModel: ActividadesViewModel = viewModel(factory = actividadFactory)
                        val actUiState by actividadViewModel.uiState.collectAsStateWithLifecycle()
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
                        val actividadViewModel: ActividadesViewModel = viewModel(factory = actividadFactory)
                        val aiState by aiViewModel.responseText.collectAsStateWithLifecycle()
                        
                        CreateGroupActivityScreen(
                            tema = temaActual,
                            grupoId = grupoId,
                            usuarioActual = currentUser!!,
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { titulo, desc ->
                                val nuevaActividad = ActividadConjunta(
                                    id = (1000..9999).random(),
                                    titulo = titulo,
                                    descripcion = desc,
                                    propietario = currentUser!!,
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
                                actividadViewModel.crearActividad(nuevaActividad)
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
                        val viewModel: ActividadesViewModel = viewModel(factory = actividadFactory)
                        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                        
                        ActivityScreen(
                            onBackClick = { navController.popBackStack() },
                            tema = temaActual,
                            uiState = uiState,
                            onNavigateToCreate = { navController.navigate("create_activity") },
                            onDeleteActivity = { act: AbstractActividad -> viewModel.eliminarActividad(act) }
                        )
                    }

                    composable("create_activity") {
                        val viewModel: ActividadesViewModel = viewModel(factory = actividadFactory)
                        
                        CreateActivityScreen(
                            tema = temaActual,
                            usuarioActual = currentUser!!,
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
                            onNavigateToSettings = { navController.navigate("settings") },
                            onNavigateToProfile = { navController.navigate("profile") }
                        )
                    }

                    composable("settings") {
                        SettingsScreen(
                            tema = temaActual,
                            selectedThemeId = temaId,
                            onThemeSelected = { newThemeId ->
                                temaId = newThemeId
                                themePreferences.setSelectedThemeId(newThemeId)
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable("profile") {
                        val user = currentUser!!
                        val savedNick = remember(user.email) { userPreferences.getNickname(user.email, user.nickname) }
                        if (savedNick.isNotBlank()) {
                            user.nickname = savedNick
                        }

                        UserProfileScreen(
                            usuario = user,
                            tema = temaActual,
                            onBackClick = { navController.popBackStack() },
                            onLogoutClick = { authViewModel.logout() },
                            onNicknameChanged = { newNick ->
                                userPreferences.setNickname(user.email, newNick)
                                user.nickname = newNick
                            }
                        )
                    }
                }
                } 
            }
        }
    }
}
