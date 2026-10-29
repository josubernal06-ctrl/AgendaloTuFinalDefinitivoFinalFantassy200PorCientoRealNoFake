# Contratos Compartidos - AgendaloTu

Este documento detalla las interfaces compartidas (contratos) disponibles en el paquete `com.example.agendalotufinaldefinitivofinalfantassy200realnofake.repository` para que los demás desarrolladores del proyecto (Persistencia, IA y UI) puedan utilizarlos como puente de comunicación e integración sin acoplar sus códigos.

Se implementó un sistema de Inyección de Dependencias manual, centralizado en `AppContainer`, y repositorios de prueba (Mocks) que sirven para trabajar en el flujo de la aplicación de manera inmediata y sin necesitar servidores reales.

## Inyección de Dependencias (`AppContainer.kt`)

Ubicación: `com.example.agendalotufinaldefinitivofinalfantassy200realnofake.di.AppContainer`

Para instanciar los repositorios en la UI (por ejemplo, en `MainActivity.kt`), se inicializa el contenedor y se accede a sus propiedades, garantizando un estado compartido.

```kotlin
val appContainer = remember { AppContainer(context) }

// Ejemplos de acceso:
val authFactory = AuthViewModel.Factory(appContainer.authRepository)
val actividadFactory = ActividadesViewModel.Factory(appContainer.actividadRepository)
```

## 1. AuthRepository (`IAuthRepository.kt`)

Se encarga de gestionar la sesión actual de la aplicación. 
El flujo simula que los usuarios de prueba inician sesión con su correo para dar acceso a la app.

*   `val currentUser: StateFlow<Usuario?>`: Proporciona un estado reactivo del usuario actualmente logueado. Retorna `null` si no hay sesión.
*   `suspend fun getTestUsers(): List<Usuario>`: Retorna una lista de usuarios de demostración disponibles.
*   `suspend fun login(email: String): Boolean`: Ejecuta el intento de inicio de sesión de manera simulada. Devuelve `true` si es exitoso.
*   `suspend fun logout()`: Elimina la sesión actual.

> **Mock provisto**: `MockAuthRepository` (por defecto provisto en AppContainer).

## 2. ActividadRepository (`IActividadRepository.kt`)

Este contrato debe ser implementado y usado por el responsable de Room/SQLite Cloud para la persistencia. Se proporcionan funciones asincrónicas básicas:

*   `fun obtenerActividades(): Flow<Resultado<List<AbstractActividad>>>`: Proporciona la escucha en tiempo real de la base de datos (con soporte de Result para cargas y errores).
*   `suspend fun insertarActividad(actividad: AbstractActividad)`: Guarda una nueva Actividad (`ActividadSolitaria` o `ActividadConjunta`) de manera asíncrona.
*   `suspend fun eliminarActividad(actividad: AbstractActividad)`: Elimina una actividad.

> **Implementación real actual**: `ActividadRepository` conectada a `AppDatabase`.

## 3. GrupoRepository (`IGrupoRepository.kt`)

Contrato para gestionar y mostrar los grupos que pertenece el usuario.

*   `fun obtenerGrupos(): Flow<Resultado<List<Grupo>>>`
*   `suspend fun crearGrupo(grupo: Grupo)`
*   `suspend fun eliminarGrupo(grupo: Grupo)`

> **Mock provisto**: `MockGrupoRepository` carga 2 grupos de ejemplo en memoria.

## 4. AiRepository (`IAiRepository.kt`)

Provee el punto de entrada para que la aplicación haga las consultas generativas a Google Gemini (y reciba recomendaciones de horarios). El encargado de la API de Gemini es responsable de sustituir el mock con la conexión real a Internet.

*   `suspend fun obtenerRespuestaGemini(promptPregunta: String): AiAnswer`: Procesa un prompt enviándolo y esperando la sugerencia.

> **Mock provisto**: `MockAiRepository` simula un retraso de red de 1 segundo y retorna un consejo genérico ("Mock recommendation...") constante.

---

### Instrucciones para el equipo (Dev 2, 3 y 4)

*   **Para UI y Navegación (Dev 4):** Si la app detecta que `currentUser` es `null`, mostrará `LoginScreen` automáticamente. Para que `MainActivity` construya correctamente nuevos `ViewModels` orientados a IA o Grupos, debes agregar sus Factories (ej. `AiViewModel.Factory(appContainer.aiRepository)`) y pasarlo al `NavHost`.
*   **Para Bases de Datos (Dev 2):** Cualquier nueva consulta a la nube (SQLite Cloud) relacionada a la agenda debe ser definida primero como un método dentro de `IActividadRepository` o `IGrupoRepository`, para luego sobrescribirse en las clases de implementación.
*   **Para la IA (Dev 3):** Simplemente enruta `AppContainer.aiRepository` para devolver un `AiRepository()` (tu implementación real que llama a Retrofit), en lugar de `MockAiRepository()`.