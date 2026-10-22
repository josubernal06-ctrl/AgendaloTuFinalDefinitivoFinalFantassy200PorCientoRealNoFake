package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.clima

import com.google.gson.annotations.SerializedName

data class ClimaDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("hourly") val hourly: HourlyDto
)

data class HourlyDto(
    @SerializedName("time") val time: List<String>,
    @SerializedName("temperature_2m") val temperature2m: List<Double>,
    @SerializedName("precipitation_probability") val precipitationProbability: List<Int>,
    @SerializedName("precipitation") val precipitation: List<Double>
)
