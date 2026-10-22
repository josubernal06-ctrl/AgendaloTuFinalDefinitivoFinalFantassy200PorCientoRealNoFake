package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayRemote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val HOLIDAY_BASE_URL = "https://date.nager.at/"

    private val clienteHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofitHoliday: Retrofit = Retrofit.Builder()
        .baseUrl(HOLIDAY_BASE_URL)
        .client(clienteHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiServiceHoliday: HolidayApiService by lazy {
        retrofitHoliday.create(HolidayApiService::class.java)
    }
}
