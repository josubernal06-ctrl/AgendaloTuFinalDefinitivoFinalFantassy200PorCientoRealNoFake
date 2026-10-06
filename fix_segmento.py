import os

file_path = "/home/josu/AndroidStudioProjects/AgendaloTuFinalDefinitivoFinalFantassy200RealNoFake/app/src/main/java/com/example/agendalotufinaldefinitivofinalfantassy200realnofake/modelos/SegmentoTiempo.kt"

with open(file_path, "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import java.time.LocalDateTime

class SegmentoTiempo(
    private var inicio: LocalDateTime,
    private var fin: LocalDateTime,
    var esConjunta: Boolean,
    var puntuacion: Double
)
""")

