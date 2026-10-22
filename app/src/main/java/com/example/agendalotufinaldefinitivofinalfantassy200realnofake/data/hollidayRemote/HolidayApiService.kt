package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayRemote

import retrofit2.http.GET
import retrofit2.http.Path

interface HolidayApiService {
    @GET("api/v3/PublicHolidays/{year}/{countryCode}")
    suspend fun getPublicHolidays(
        @Path("year") year: Int = 2026,
        @Path("countryCode") countryCode: String,
    ): List<HolidayDto>
}