package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.firebase

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.repository.Resultado
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.FireBaseUsuario
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    /** Usuario con la sesion abierta ahora mismo, o null si no hay sesion. Es sincrono. */
    fun usuarioActual(): Usuario? = auth.currentUser?.let { firebaseUser ->
        val fbUsuario = FireBaseUsuario(uid = firebaseUser.uid, email = firebaseUser.email.orEmpty())
        Usuario(
            id = fbUsuario.uid.hashCode(),
            nickname = fbUsuario.email.substringBefore("@").ifEmpty { "Usuario" },
            email = fbUsuario.email
        )
    }

    /** Emite el usuario cada vez que se inicia o se cierra la sesion (null = sin sesion). */
    fun observarSesion(): Flow<Usuario?> = callbackFlow {
        val oyente = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.let { firebaseUser ->
                val fbUsuario = FireBaseUsuario(uid = firebaseUser.uid, email = firebaseUser.email.orEmpty())
                Usuario(
                    id = fbUsuario.uid.hashCode(),
                    nickname = fbUsuario.email.substringBefore("@").ifEmpty { "Usuario" },
                    email = fbUsuario.email
                )
            })
        }
        auth.addAuthStateListener(oyente)
        awaitClose { auth.removeAuthStateListener(oyente) }
    }

    suspend fun iniciarSesion(email: String, contrasena: String): Resultado<Unit> =
        llamar { auth.signInWithEmailAndPassword(email, contrasena).await() }

    suspend fun registrar(email: String, contrasena: String): Resultado<Unit> =
        llamar { auth.createUserWithEmailAndPassword(email, contrasena).await() }

    suspend fun enviarCorreoDeRecuperacion(email: String): Resultado<Unit> =
        llamar { auth.sendPasswordResetEmail(email).await() }

    fun cerrarSesion() = auth.signOut()

    /** Ejecuta la llamada y traduce cualquier excepcion de Firebase a un [Resultado.Error]. */
    private suspend fun llamar(bloque: suspend () -> Unit): Resultado<Unit> =
        try {
            bloque()
            Resultado.Exito(Unit)
        } catch (e: CancellationException) {
            throw e // la cancelacion no es un error: debe propagarse
        } catch (e: Exception) {
            Resultado.Error(RuntimeException(traducir(e), e))
        }

    private fun traducir(e: Exception): String = when (e) {
        is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil. Usa al menos 6 caracteres."
        is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con ese correo."
        is FirebaseAuthInvalidUserException -> "No existe una cuenta con ese correo."
        is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
        is FirebaseNetworkException -> "No hay conexión a internet. Revisa tu red e inténtalo de nuevo."
        else -> "No se pudo completar la operación. Inténtalo de nuevo."
    }
}
