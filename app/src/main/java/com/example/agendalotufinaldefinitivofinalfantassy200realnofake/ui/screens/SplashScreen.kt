package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor

@Composable
fun SplashScreen(tema: TemaColor, onSplashFinished: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(2500L) // 2.5 segundos mostrando la imagen
        onSplashFinished()
    }

    val context = LocalContext.current
    val splashId = context.resources.getIdentifier("splash_image", "drawable", context.packageName)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tema.fondo),
        contentAlignment = Alignment.Center
    ) {
        if (splashId != 0) {
            Image(
                painter = painterResource(id = splashId),
                contentDescription = "Splash Screen AgendaloTu",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "Guarda la imagen enviada como 'splash_image.png'\ndentro de app/src/main/res/drawable/",
                color = tema.letras,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}