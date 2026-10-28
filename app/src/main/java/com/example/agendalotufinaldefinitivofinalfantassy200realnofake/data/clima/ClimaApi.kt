package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.clima

import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApi {
    @GET("v1/forecast")
    suspend fun obtenerClima(
        @Query("latitude") latitude: Double = -16.5000,
        @Query("longitude") longitude: Double = -68.1500,
        @Query("hourly") hourly: String = "precipitation_probability,precipitation,temperature_2m",
        @Query("timezone") timezone: String = "auto",
        @Query("forecast_days") forecastDays: Int = 16
    ): ClimaDto
}
