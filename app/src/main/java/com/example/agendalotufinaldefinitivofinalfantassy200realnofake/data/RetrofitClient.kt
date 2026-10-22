package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.ai.AiApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** 
 * Unica instancia de Retrofit de la app (patron singleton con `object`). 
 * Nos conectará a la web.
 */
object RetrofitClient {

    // La URL base debe terminar en "/" para que Retrofit pueda concatenar las rutas.
    private const val BASE_URL = "https://api.chucknorris.io/"
    private const val BASE_URL3 = "https://stoic.tekloon.net/"
    private const val BASE_URL_KANYE = "https://api.kanye.rest/"

    // La API Key en la URL hace que Google interprete el "v1beta" distinto si le mandamos "v1" o "v1beta".
    // Asegurémonos de tener la barra al final para Retrofit.
    private const val BASE_URL_GEMINI = "https://generativelanguage.googleapis.com/v1beta/"

    // Cliente HTTP con timeouts extendidos para Gemini (las IA requieren mas tiempo de respuesta)
    private val clienteHttpGemini: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    // Inicialización "lazy" (perezosa): Solo se crea cuando se la llama por primera vez



    val instanceGemini: AiApi by lazy {
        Retrofit.Builder() // builder es un patron de diseño de codigo
            .baseUrl(BASE_URL_GEMINI) // Le pasamos la url
            .client(clienteHttpGemini) // Le pasamos el cliente configurado con timeouts largos
            .addConverterFactory(GsonConverterFactory.create()) // El conversor mágico que pasa de JSON a Kotlin
            .build() //constructor del objecto Retrofit
            .create(AiApi::class.java) // Crea la implementación de nuestra Interfaz

    }

}
