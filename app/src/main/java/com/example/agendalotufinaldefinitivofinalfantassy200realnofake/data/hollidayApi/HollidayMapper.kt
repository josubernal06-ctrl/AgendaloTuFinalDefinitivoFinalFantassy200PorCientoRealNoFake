package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayApi

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayRemote.HolidayDto
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.model.Holiday

fun HolidayDto.toModel(): Holiday = Holiday(
    name = localName ?: name ?: "Sin nombre",
    date = date ?: ""
)
