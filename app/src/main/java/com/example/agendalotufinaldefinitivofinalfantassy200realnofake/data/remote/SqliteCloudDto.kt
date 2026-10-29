package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Petición para ejecutar SQL en la API de SQLite Cloud (Weblite)
 */
data class SqliteCloudRequest(
    @SerializedName("database") val database: String = "agendalo.sqlite",
    @SerializedName("sql") val sql: String
)

/**
 * Respuesta devuelta por la API de SQLite Cloud
 */
data class SqliteCloudResponse(
    @SerializedName("status") val status: String? = null,
    @SerializedName("data") val data: Any? = null,
    @SerializedName("error") val error: String? = null
)
