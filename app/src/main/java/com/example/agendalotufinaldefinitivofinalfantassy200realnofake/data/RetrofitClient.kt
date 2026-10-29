package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiApi
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.clima.ClimaApi
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayRemote.HolidayApiService
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.SqliteCloudApi
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // --- Gemini API ---
    private const val BASE_URL_GEMINI = "https://generativelanguage.googleapis.com/v1beta/"

    private val clienteHttpGemini: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()


    val instanceGemini: AiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_GEMINI)
            .client(clienteHttpGemini)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AiApi::class.java)
    }

    // --- Open-Meteo Clima API ---
    private const val BASE_URL_OPEN_METEO = "https://api.open-meteo.com/"

    private val clienteHttpClima: OkHttpClient = OkHttpClient.Builder()
        .protocols(listOf(Protocol.HTTP_1_1))
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (Android 12; Mobile; rv:124.0) Gecko/124.0 Firefox/124.0")
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .retryOnConnectionFailure(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: ClimaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_OPEN_METEO)
            .client(clienteHttpClima)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClimaApi::class.java)
    }

    // --- Holiday API ---
    private const val HOLIDAY_BASE_URL = "https://date.nager.at/"

    private val clienteHttpHoliday: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val apiServiceHoliday: HolidayApiService by lazy {
        Retrofit.Builder()
            .baseUrl(HOLIDAY_BASE_URL)
            .client(clienteHttpHoliday)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HolidayApiService::class.java)
    }

    // --- SQLite Cloud API ---
    private const val BASE_URL_SQLITE_CLOUD = "https://cpokzlqcdk.g3.sqlite.cloud/"

    private val clienteHttpSqliteCloud: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    val sqliteCloudApi: SqliteCloudApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_SQLITE_CLOUD)
            .client(clienteHttpSqliteCloud)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SqliteCloudApi::class.java)
    }
}
