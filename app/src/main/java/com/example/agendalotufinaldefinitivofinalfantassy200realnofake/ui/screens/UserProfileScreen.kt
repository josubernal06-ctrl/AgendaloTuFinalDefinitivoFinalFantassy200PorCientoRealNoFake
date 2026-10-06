package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.components.BotonPersonalizado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.TemaColor
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.ui.theme.obtenerTema

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    usuario: Usuario,
    tema: TemaColor,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", color = tema.letras) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = tema.letras
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = tema.botones,
                )
            )
        },
        containerColor = tema.fondo
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Placeholder circular
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(tema.detalles.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar del usuario",
                    modifier = Modifier.size(80.dp),
                    tint = tema.letras
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Nombre del usuario
            Text(
                text = usuario.nickname,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = tema.letras
            )
            
            // Estado de disponibilidad usando su método getDisponibilidad()
            Text(
                text = if (usuario.getDisponibilidad()) "Disponible" else "Ocupado",
                fontSize = 16.sp,
                color = if (usuario.getDisponibilidad()) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjeta con la información extra del usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = tema.detalles.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Datos de la cuenta",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = tema.letras,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    PerfilDatoItem(
                        icono = Icons.Default.Email,
                        titulo = "Correo Electrónico",
                        valor = usuario.email,
                        tema = tema
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PerfilDatoItem(
                        icono = Icons.Default.Info,
                        titulo = "ID de Usuario",
                        valor = "#Oculto",
                        tema = tema
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botones de acción aprovechando el componente BotonPersonalizado de AppButtons.kt
            BotonPersonalizado(
                texto = "Editar Perfil",
                onClick = { /* TODO: Implementar edición */ },
                colorFondo = tema.botones,
                colorTexto = tema.fondo
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            BotonPersonalizado(
                texto = "Cerrar Sesión",
                onClick = { /* TODO: Implementar cierre de sesión */ },
                colorFondo = tema.detalles.copy(alpha = 0.5f),
                colorTexto = tema.letras
            )
        }
    }
}

@Composable
fun PerfilDatoItem(
    icono: ImageVector,
    titulo: String,
    valor: String,
    tema: TemaColor
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = tema.botones,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = tema.letras.copy(alpha = 0.7f)
            )
            Text(
                text = valor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = tema.letras
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    // Datos y tema falsos para la vista previa
    val usuarioEjemplo = Usuario(1, nickname = "Juan Pérez", email = "juan.perez@email.com")
    val temaEjemplo = obtenerTema(1) 
    
    UserProfileScreen(
        usuario = usuarioEjemplo,
        tema = temaEjemplo,
        onBackClick = {}
    )
}
