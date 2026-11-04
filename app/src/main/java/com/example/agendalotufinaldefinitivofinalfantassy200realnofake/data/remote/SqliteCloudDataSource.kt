package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote

import android.util.Log
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.GrupoEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.RetrofitClient

/**
 * FUENTE DE DATOS REMOTA PARA SQLITE CLOUD (HTTP API WEBLITE).
 * Ejecuta consultas SQL en la base de datos remota agendalo.sqlite vía Retrofit.
 * Maneja respaldo de endpoints, consultas para grupos, miembros, actividades y nicknames.
 */
class SqliteCloudDataSource(
    private val api: SqliteCloudApi = RetrofitClient.sqliteCloudApi
) {
    private val candidateUrls = listOf(
        "https://cpokzlqcdk.g3.sqlite.cloud/v1/weblite/sql",
        "https://cpokzlqcdk.sqlite.cloud/v1/weblite/sql"
    )

    private var workingUrl: String? = "https://cpokzlqcdk.g3.sqlite.cloud/v1/weblite/sql"

    /**
     * Ejecuta cualquier consulta de modificación (INSERT, UPDATE, DELETE) en SQLite Cloud.
     * Retorna true si la respuesta HTTP de SQLite Cloud fue exitosa (200 OK).
     */
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

    /**
     * Busca un grupo en SQLite Cloud filtrando por su codigoInvitacion.
     * Retorna el GrupoEntity encontrado o null si la consulta HTTP es exitosa pero no hay filas.
     * Lanza excepción si ocurre un error HTTP o de conexión para diferenciar no-existencia de fallas de red.
     */
    suspend fun obtenerGrupoPorCodigoRemoto(apiKey: String, codigo: String): GrupoEntity? {
        if (apiKey.isBlank()) throw IllegalArgumentException("API Key está vacía")
        
        val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
        val cleanCodigo = codigo.replace("'", "''")
        val sql = "USE DATABASE agendalo.sqlite; SELECT id, nombreGrupo, descripcion, propietarioId, codigoInvitacion FROM `GROUP` WHERE codigoInvitacion = '$cleanCodigo';"
        val requestPayload = SqliteCloudRequest(database = "agendalo.sqlite", sql = sql)

        var lastException: Exception? = null

        for (url in candidateUrls) {
            try {
                val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                Log.d("SqliteCloud", "obtenerGrupoPorCodigoRemoto response code: ${resp.code()}, body: ${resp.body()}")
                
                if (resp.isSuccessful) {
                    val bodyData = resp.body()?.data
                    if (bodyData == null) {
                        // Respuesta válida pero sin data significa que no existe el grupo
                        return null
                    }
                    
                    val data = bodyData as? List<*>
                    if (data.isNullOrEmpty()) {
                        // Respuesta válida pero lista vacía significa que no existe el grupo
                        return null
                    }

                    val firstRow = data[0]
                    if (firstRow is Map<*, *>) {
                        val parsedId = firstRow["id"].toIntSafe().let { if (it != 0) it else firstRow["ID"].toIntSafe() }
                        if (parsedId == 0) throw IllegalStateException("ID inválido o cero recibido de SQLite Cloud para Map")
                        
                        return GrupoEntity(
                            id = parsedId,
                            nombreGrupo = (firstRow["nombreGrupo"] as? String) ?: (firstRow["NOMBREGRUPO"] as? String) ?: "Grupo",
                            descripcion = (firstRow["descripcion"] as? String) ?: (firstRow["DESCRIPCION"] as? String) ?: "",
                            propietarioId = (firstRow["propietarioId"] as? String) ?: (firstRow["PROPIETARIOID"] as? String) ?: "",
                            codigoInvitacion = (firstRow["codigoInvitacion"] as? String) ?: (firstRow["CODIGOINVITACION"] as? String) ?: codigo
                        )
                    } else if (firstRow is List<*>) {
                        val parsedId = firstRow.getOrNull(0).toIntSafe()
                        if (parsedId == 0) throw IllegalStateException("ID inválido o cero recibido de SQLite Cloud para List")
                        
                        return GrupoEntity(
                            id = parsedId,
                            nombreGrupo = (firstRow.getOrNull(1) as? String) ?: "Grupo",
                            descripcion = (firstRow.getOrNull(2) as? String) ?: "",
                            propietarioId = (firstRow.getOrNull(3) as? String) ?: "",
                            codigoInvitacion = (firstRow.getOrNull(4) as? String) ?: codigo
                        )
                    } else {
                        throw IllegalStateException("Formato de fila inesperado de SQLite Cloud: ${firstRow?.javaClass?.name}")
                    }
                } else {
                    lastException = RuntimeException("Error HTTP ${resp.code()}: ${resp.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SqliteCloud", "Error en url $url: ${e.message}", e)
                lastException = e
            }
        }
        
        // Si agotó las URLs y ninguna respondió isSuccessful, entonces propaga la excepción
        throw lastException ?: RuntimeException("Fallo desconocido al conectar con SQLite Cloud")
    }

    /** Consulta la lista de UIDs de usuarios que son miembros de un grupo específico en la tabla GROUP_MEMBER. */
    suspend fun obtenerMiembrosDeGrupoRemoto(apiKey: String, grupoId: Int): List<String> {
        val result = mutableListOf<String>()
        try {
            if (apiKey.isBlank() || grupoId <= 0) return result
            val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
            val sql = "USE DATABASE agendalo.sqlite; SELECT userId FROM `GROUP_MEMBER` WHERE grupoId = $grupoId;"
            val requestPayload = SqliteCloudRequest(database = "agendalo.sqlite", sql = sql)

            for (url in candidateUrls) {
                try {
                    val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                    if (resp.isSuccessful && resp.body()?.data != null) {
                        val data = resp.body()?.data as? List<*>
                        if (!data.isNullOrEmpty()) {
                            for (row in data) {
                                if (row is Map<*, *>) {
                                    val uid = (row["userId"] as? String) ?: (row["USERID"] as? String)
                                    if (!uid.isNullOrBlank()) result.add(uid)
                                } else if (row is List<*>) {
                                    val uid = row.getOrNull(0) as? String
                                    if (!uid.isNullOrBlank()) result.add(uid)
                                }
                            }
                            return result
                        }
                    }
                } catch (_: Exception) {}
            }
        } catch (e: Exception) {
            Log.e("SqliteCloud", "Error obteniendo miembros remotos: ${e.message}", e)
        }
        return result
    }

    /**
     * Consulta todos los grupos asociados al userUid actual (como propietario o como miembro en GROUP_MEMBER).
     * Retorna null si la solicitud HTTP/red falla para evitar limpiar la caché local por un error de conexión.
     */
    suspend fun obtenerGruposRemotosDeUsuario(apiKey: String, userUid: String): List<GrupoEntity>? {
        if (apiKey.isBlank() || userUid.isBlank()) return null
        val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
        val cleanUid = userUid.replace("'", "''")
        val sql = "USE DATABASE agendalo.sqlite; SELECT g.id, g.nombreGrupo, g.descripcion, g.propietarioId, g.codigoInvitacion FROM `GROUP` g JOIN `GROUP_MEMBER` gm ON g.id = gm.grupoId WHERE gm.userId = '$cleanUid' UNION SELECT id, nombreGrupo, descripcion, propietarioId, codigoInvitacion FROM `GROUP` WHERE propietarioId = '$cleanUid';"
        val requestPayload = SqliteCloudRequest(database = "agendalo.sqlite", sql = sql)

        var lastException: Exception? = null

        for (url in candidateUrls) {
            try {
                val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                if (resp.isSuccessful) {
                    val result = mutableListOf<GrupoEntity>()
                    val data = resp.body()?.data as? List<*>
                    if (!data.isNullOrEmpty()) {
                        for (row in data) {
                            if (row is Map<*, *>) {
                                val idVal = row["id"].toIntSafe().let { if (it != 0) it else row["ID"].toIntSafe() }
                                val nombre = (row["nombreGrupo"] as? String) ?: (row["NOMBREGRUPO"] as? String) ?: "Grupo"
                                val desc = (row["descripcion"] as? String) ?: (row["DESCRIPCION"] as? String) ?: ""
                                val owner = (row["propietarioId"] as? String) ?: (row["PROPIETARIOID"] as? String) ?: ""
                                val codigo = (row["codigoInvitacion"] as? String) ?: (row["CODIGOINVITACION"] as? String) ?: ""
                                result.add(GrupoEntity(idVal, nombre, desc, owner, codigo))
                            } else if (row is List<*>) {
                                val idVal = row.getOrNull(0).toIntSafe()
                                val nombre = (row.getOrNull(1) as? String) ?: "Grupo"
                                val desc = (row.getOrNull(2) as? String) ?: ""
                                val owner = (row.getOrNull(3) as? String) ?: ""
                                val codigo = (row.getOrNull(4) as? String) ?: ""
                                result.add(GrupoEntity(idVal, nombre, desc, owner, codigo))
                            }
                        }
                    }
                    return result
                } else {
                    lastException = RuntimeException("Error HTTP ${resp.code()}: ${resp.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                lastException = e
            }
        }
        Log.e("SqliteCloud", "Error obteniendo grupos remotos de usuario: ${lastException?.message}")
        return null
    }

    private fun Any?.toIntSafe(): Int = when (this) {
        is Double -> this.toInt()
        is Float -> this.toInt()
        is Number -> this.toInt()
        is String -> this.toDoubleOrNull()?.toInt() ?: 0
        else -> 0
    }

    /**
     * Consulta las actividades autorizadas desde la tabla ACTIVITY de SQLite Cloud para el userUid actual.
     * Recupera tanto las personales (userId = userUid) como las compartidas (grupoId en sus membresías).
     */
    suspend fun obtenerActividadesRemotas(apiKey: String, userUid: String): List<com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadEntity> {
        val result = mutableListOf<com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadEntity>()
        try {
            if (apiKey.isBlank() || userUid.isBlank()) return result
            val authHeader = if (apiKey.startsWith("Bearer ")) apiKey else "Bearer $apiKey"
            val cleanUid = userUid.replace("'", "''")
            
            val remoteNicknames = obtenerNicknamesRemotos(apiKey)

            val sql = "USE DATABASE agendalo.sqlite; SELECT id, titulo, descripcion, userId, grupoId, esConjunta, esPrivada, fechaInicio, fechaFin FROM ACTIVITY WHERE userId = '$cleanUid' OR grupoId IN (SELECT grupoId FROM GROUP_MEMBER WHERE userId = '$cleanUid');"
            val requestPayload = SqliteCloudRequest(database = "agendalo.sqlite", sql = sql)

            for (url in candidateUrls) {
                try {
                    val resp = api.ejecutarSqlConUrl(url, authHeader, requestPayload)
                    if (resp.isSuccessful && resp.body()?.data != null) {
                        val data = resp.body()?.data as? List<*>
                        if (!data.isNullOrEmpty()) {
                            for (row in data) {
                                if (row is Map<*, *>) {
                                    val idVal = row["id"].toIntSafe().let { if (it != 0) it else row["ID"].toIntSafe() }
                                    val titulo = (row["titulo"] as? String) ?: (row["TITULO"] as? String) ?: "Actividad"
                                    val desc = (row["descripcion"] as? String) ?: (row["DESCRIPCION"] as? String) ?: ""
                                    val uId = (row["userId"] as? String) ?: (row["USERID"] as? String) ?: cleanUid
                                    val propId = uId.hashCode()
                                    val propNick = remoteNicknames[uId] ?: uId.substringBefore("@").ifEmpty { "Usuario" }
                                    val gId = row["grupoId"].toIntSafe().takeIf { it > 0 } ?: row["GRUPOID"].toIntSafe().takeIf { it > 0 }
                                    val esConj = (row["esConjunta"].toIntSafe() == 1) || (gId != null && gId > 0)
                                    val priv = (row["esPrivada"].toIntSafe() == 1)
                                    val inicio = (row["fechaInicio"] as? String) ?: (row["FECHAINICIO"] as? String) ?: ""
                                    val fin = (row["fechaFin"] as? String) ?: (row["FECHAFIN"] as? String) ?: ""
                                    
                                    if (inicio.isNotBlank()) {
                                        result.add(com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadEntity(
                                            id = idVal,
                                            titulo = titulo,
                                            descripcion = desc,
                                            esPrivada = priv,
                                            propietarioId = propId,
                                            propietarioNickname = propNick,
                                            inicio = inicio,
                                            fin = if (fin.isNotBlank()) fin else inicio,
                                            esConjunta = esConj,
                                            puntuacion = 0.0,
                                            grupoId = gId
                                        ))
                                    }
                                } else if (row is List<*>) {
                                    val idVal = row.getOrNull(0).toIntSafe()
                                    val titulo = (row.getOrNull(1) as? String) ?: "Actividad"
                                    val desc = (row.getOrNull(2) as? String) ?: ""
                                    val uId = (row.getOrNull(3) as? String) ?: cleanUid
                                    val propId = uId.hashCode()
                                    val propNick = remoteNicknames[uId] ?: uId.substringBefore("@").ifEmpty { "Usuario" }
                                    val gId = row.getOrNull(4).toIntSafe().takeIf { it > 0 }
                                    val esConj = (row.getOrNull(5).toIntSafe() == 1) || (gId != null && gId > 0)
                                    val priv = (row.getOrNull(6).toIntSafe() == 1)
                                    val inicio = (row.getOrNull(7) as? String) ?: ""
                                    val fin = (row.getOrNull(8) as? String) ?: ""
                                    
                                    if (inicio.isNotBlank()) {
                                        result.add(com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.ActividadEntity(
                                            id = idVal,
                                            titulo = titulo,
                                            descripcion = desc,
                                            esPrivada = priv,
                                            propietarioId = propId,
                                            propietarioNickname = propNick,
                                            inicio = inicio,
                                            fin = if (fin.isNotBlank()) fin else inicio,
                                            esConjunta = esConj,
                                            puntuacion = 0.0,
                                            grupoId = gId
                                        ))
                                    }
                                }
                            }
                            return result
                        }
                    }
                } catch (_: Exception) {}
            }
        } catch (e: Exception) {
            Log.e("SqliteCloud", "Error obteniendo actividades remotas: ${e.message}", e)
        }
        return result
    }

    /** Consulta la tabla USER de SQLite Cloud para construir un mapa de (uid -> nickname, email -> nickname). */
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
