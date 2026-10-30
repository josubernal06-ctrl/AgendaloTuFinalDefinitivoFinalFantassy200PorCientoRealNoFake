package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.navaigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens.LoginScreen
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.viewmodels.AuthViewModel

private const val RUTA_LOGIN = "login"
private const val RUTA_HOME = "home"

@Composable
fun AppNavigation(authViewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val usuario by authViewModel.usuario.collectAsState()
    val haySesion = usuario != null
    NavHost(
        navController = navController,
        startDestination = if (haySesion) RUTA_HOME else RUTA_LOGIN
    ) {
        composable(RUTA_LOGIN) {
            LoginScreen(viewModel = authViewModel)
        }
        composable(RUTA_HOME) {
            // Home stub route
        }
    }
    LaunchedEffect(haySesion) {
        val destinoActual = navController.currentDestination?.route
        if (haySesion && destinoActual == RUTA_LOGIN) {
            navController.navigate(RUTA_HOME) { popUpTo(0) }
        } else if (!haySesion && destinoActual != RUTA_LOGIN) {
            navController.navigate(RUTA_LOGIN) { popUpTo(0) }
        }
    }
}
