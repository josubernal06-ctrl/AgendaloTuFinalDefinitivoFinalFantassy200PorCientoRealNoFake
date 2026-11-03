package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote

import android.util.Log
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoEntity
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

            Log.d("SqliteCloud", "Enviando SQL a SQLite Cloud: $fullSql")

            // Reutilizar el endpoint funcional directamente
            workingUrl?.let { url ->
                val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                if (resp.isSuccessful) {
                    Log.d("SqliteCloud", "¡ÉXITO EN SQLITE CLOUD ($url)! Respuesta: ${resp.body()}")
                    return true
                } else {
                    Log.e("SqliteCloud", "Error HTTP ${resp.code()}: ${resp.errorBody()?.string()}")
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

    suspend fun obtenerGrupoPorCodigoRemoto(apiKey: String, codigo: String): GrupoEntity? {
        return try {
            if (apiKey.isBlank()) return null
            val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
            val cleanCodigo = codigo.replace("'", "''")
            val sql = "USE DATABASE agendalo.sqlite; SELECT id, nombreGrupo, descripcion, propietarioId, codigoInvitacion FROM `GROUP` WHERE codigoInvitacion = '$cleanCodigo';"
            val requestPayload = SqliteCloudRequest(database = "agendalo.sqlite", sql = sql)

            for (url in candidateUrls) {
                try {
                    val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                    Log.d("SqliteCloud", "obtenerGrupoPorCodigoRemoto response code: ${resp.code()}, body: ${resp.body()}")
                    if (resp.isSuccessful && resp.body()?.data != null) {
                        val body = resp.body()
                        val data = body?.data as? List<*>
                        if (!data.isNullOrEmpty()) {
                            val firstRow = data[0]
                            if (firstRow is Map<*, *>) {
                                return GrupoEntity(
                                    id = (firstRow["id"] as? Number)?.toInt() ?: (firstRow["ID"] as? Number)?.toInt() ?: 0,
                                    nombreGrupo = (firstRow["nombreGrupo"] as? String) ?: (firstRow["NOMBREGRUPO"] as? String) ?: "Grupo",
                                    descripcion = (firstRow["descripcion"] as? String) ?: (firstRow["DESCRIPCION"] as? String) ?: "",
                                    propietarioId = (firstRow["propietarioId"] as? String) ?: (firstRow["PROPIETARIOID"] as? String) ?: "",
                                    codigoInvitacion = (firstRow["codigoInvitacion"] as? String) ?: (firstRow["CODIGOINVITACION"] as? String) ?: codigo
                                )
                            } else if (firstRow is List<*>) {
                                return GrupoEntity(
                                    id = (firstRow.getOrNull(0) as? Number)?.toInt() ?: 0,
                                    nombreGrupo = (firstRow.getOrNull(1) as? String) ?: "Grupo",
                                    descripcion = (firstRow.getOrNull(2) as? String) ?: "",
                                    propietarioId = (firstRow.getOrNull(3) as? String) ?: "",
                                    codigoInvitacion = (firstRow.getOrNull(4) as? String) ?: codigo
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SqliteCloud", "Error en url $url: ${e.message}", e)
                }
            }
            null
        } catch (e: Exception) {
            Log.e("SqliteCloud", "Error obteniendo grupo remoto: ${e.message}", e)
            null
        }
    }

    suspend fun obtenerNicknamesRemotos(apiKey: String): Map<String, String> {
        val result = mutableMapOf<String, String>()
        try {
            if (apiKey.isBlank()) return result
            val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
            val sql = "USE DATABASE agendalo.sqlite; SELECT uid, nickname, email FROM USER;"
            val requestPayload = SqliteCloudRequest(database = "agendalo.sqlite", sql = sql)

            for (url in candidateUrls) {
                try {
                    val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                    if (resp.isSuccessful && resp.body()?.data != null) {
                        val data = resp.body()?.data as? List<*>
                        if (!data.isNullOrEmpty()) {
                            for (row in data) {
                                if (row is Map<*, *>) {
                                    val uid = (row["uid"] as? String) ?: (row["email"] as? String) ?: continue
                                    val nick = (row["nickname"] as? String) ?: (row["email"] as? String)?.substringBefore("@") ?: "Usuario"
                                    if (uid.isNotBlank() && nick.isNotBlank()) {
                                        result[uid] = nick
                                        (row["email"] as? String)?.let { email -> result[email] = nick }
                                    }
                                } else if (row is List<*>) {
                                    val uid = row.getOrNull(0) as? String ?: row.getOrNull(1) as? String ?: continue
                                    val nick = (row.getOrNull(2) as? String) ?: (row.getOrNull(1) as? String)?.substringBefore("@") ?: "Usuario"
                                    if (uid.isNotBlank() && nick.isNotBlank()) {
                                        result[uid] = nick
                                    }
                                }
                            }
                            return result
                        }
                    }
                } catch (_: Exception) {}
            }
        } catch (e: Exception) {
            Log.e("SqliteCloud", "Error obteniendo nicknames remotos: ${e.message}", e)
        }
        return result
    }
}
