package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayApi

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.RetrofitClient
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayRemote.HolidayApiService
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Holiday

class HolidayRepository {
    private val api: HolidayApiService = RetrofitClient.apiServiceHoliday

    suspend fun getHolidays(countryCode: String = "BO", year: Int = 2026): Result<List<Holiday>> {
        return runCatching {
            val list = api.getPublicHolidays(year, countryCode)
            list.map { it.toModel() }
        }
    }

    suspend fun getRandomHoliday(countryCode: String, year: Int = 2026): Result<Holiday> {
        return runCatching {
            val list = api.getPublicHolidays(year, countryCode)
            if (list.isEmpty()) {
                throw Exception("No hay feriados para $countryCode")
            }
            list.random().toModel()
        }
    }
}
