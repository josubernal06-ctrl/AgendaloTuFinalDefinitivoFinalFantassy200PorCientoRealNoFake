package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

/**
 * Interfaz Retrofit para la API HTTP Weblite de SQLite Cloud
 */
interface SqliteCloudApi {

    @POST
    suspend fun ejecutarSqlConUrl(
        @Url url: String,
        @Header("Authorization") apiKey: String,
        @Body request: SqliteCloudRequest
    ): Response<SqliteCloudResponse>
}
