package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote

import android.util.Log
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.RetrofitClient

/**
 * Fuente de datos remota para ejecutar consultas SQL en SQLite Cloud sin bloquear la app si no hay red.
 */
class SqliteCloudDataSource(
    private val api: SqliteCloudApi = RetrofitClient.sqliteCloudApi
) {
    private val candidateUrls = listOf(
        "https://cpokzlqcdk.g3.sqlite.cloud/v1/weblite/sql",
        "https://cpokzlqcdk.sqlite.cloud/v1/weblite/sql"
    )

    private var workingUrl: String? = "https://cpokzlqcdk.g3.sqlite.cloud/v1/weblite/sql"

    suspend fun ejecutarConsulta(apiKey: String, sqlQuery: String): Boolean {
        return try {
            if (apiKey.isBlank()) {
                Log.e("SqliteCloud", "SQLITE_CLOUD_KEY está vacía en local.properties")
                return false
            }

            val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
            val fullSql = if (!sqlQuery.contains("USE DATABASE", ignoreCase = true)) {
                "USE DATABASE agendalo.sqlite; $sqlQuery"
            } else {
                sqlQuery
            }

            val requestPayload = SqliteCloudRequest(database = "agendalo.sqlite", sql = fullSql)

            // Reutilizar el endpoint funcional directamente
            workingUrl?.let { url ->
                val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                if (resp.isSuccessful) {
                    Log.d("SqliteCloud", "¡ÉXITO EN SQLITE CLOUD ($url)! Respuesta: ${resp.body()}")
                    return true
                }
            }

            // Probar candidato de respaldo en caso de fallo
            for (url in candidateUrls) {
                try {
                    val response = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                    if (response.isSuccessful) {
                        workingUrl = url
                        Log.d("SqliteCloud", "¡ENDPOINT CORRECTO ENCONTRADO ($url)! Respuesta: ${response.body()}")
                        return true
                    }
                } catch (e: Exception) {
                    Log.w("SqliteCloud", "Falló conexión con $url: ${e.message}")
                }
            }

            false
        } catch (e: Exception) {
            Log.e("SqliteCloud", "Excepción al conectar a SQLite Cloud: ${e.message}", e)
            false
        }
    }
}
