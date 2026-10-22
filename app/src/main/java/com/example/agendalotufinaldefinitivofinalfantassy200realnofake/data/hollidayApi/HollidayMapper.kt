package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayApi

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.hollidayRemote.HolidayDto
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Holiday

fun HolidayDto.toModel(): Holiday = Holiday(
    name = localName ?: name ?: "Sin nombre",
    date = date ?: ""
)
