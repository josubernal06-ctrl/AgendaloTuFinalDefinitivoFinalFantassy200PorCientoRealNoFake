package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.remote.HolidayDto
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.model.Holiday

fun HolidayDto.toModel(): Holiday = Holiday(
    name = localName ?: name ?: "Sin nombre",
    date = date ?: ""
)
