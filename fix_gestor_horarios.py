import os
import shutil

base_dir = "/home/josu/AndroidStudioProjects/AgendaloTuFinalDefinitivoFinalFantassy200RealNoFake/app/src/main/java/com/example/agendalotufinaldefinitivofinalfantassy200realnofake"
logica_dir = os.path.join(base_dir, "logica")

# Rename the file if needed
if os.path.exists(os.path.join(logica_dir, "GestionHorarios.kt")):
    os.rename(os.path.join(logica_dir, "GestionHorarios.kt"), os.path.join(logica_dir, "GestorHorarios.kt"))

with open(os.path.join(logica_dir, "GestorHorarios.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.logica

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.*

class GestorHorarios {
    fun calcularMejorHorario(g: Grupo, a: AbstractActividad, dur: Int): RecomendacionHorario? {
        // Implementación dummy para compilar y cumplir con el diagrama
        return null
    }
}
""")

