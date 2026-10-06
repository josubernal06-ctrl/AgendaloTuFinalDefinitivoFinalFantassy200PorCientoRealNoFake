import os
import shutil

base_dir = "/home/josu/AndroidStudioProjects/AgendaloTuFinalDefinitivoFinalFantassy200RealNoFake/app/src/main/java/com/example/agendalotufinaldefinitivofinalfantassy200realnofake/modelos"
os.makedirs(base_dir, exist_ok=True)

# Usuario
with open(os.path.join(base_dir, "Usuario.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces.IOrganizador
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces.IParticipante

class Usuario(
    private val id: Int,
    var nickname: String,
    var email: String
) : IParticipante, IOrganizador {

    var preferencias: PreferenciasUsuario = PreferenciasUsuario(mutableListOf())

    override fun notificar(mensaje: String) {
        // Implementación
    }

    override fun crearGrupo(nombre: String) {
        // Implementación
    }

    override fun invitarMiembro(user: Usuario) {
        // Implementación
    }

    fun crearActividad() {
        // Implementación
    }

    fun getDisponibilidad(): Boolean {
        // Implementación
        return true
    }
}
""")

# PreferenciasUsuario
with open(os.path.join(base_dir, "PreferenciasUsuario.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class PreferenciasUsuario(
    var listaPreferencias: List<Pref_Horario> = emptyList()
)
""")

# Pref_Horario
with open(os.path.join(base_dir, "Pref_Horario.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import java.time.DayOfWeek
import java.time.LocalTime

class Pref_Horario(
    var dia: DayOfWeek,
    private var horaInicio: LocalTime,
    private var horaFin: LocalTime,
    var prioridad: Int
)
""")

# Grupo
with open(os.path.join(base_dir, "Grupo.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class Grupo(
    private val id: Int,
    var nombreGrupo: String,
    private var propietario: Usuario,
    var miembros: List<Usuario> = emptyList()
) {
    fun agregarMiembro(user: Usuario) {
        if (miembros is MutableList) {
            (miembros as MutableList).add(user)
        }
    }

    fun eliminarMiembro(user: Usuario) {
        if (miembros is MutableList) {
            (miembros as MutableList).remove(user)
        }
    }
}
""")

# SegmentoTiempo
with open(os.path.join(base_dir, "SegmentoTiempo.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

import java.time.LocalDateTime

class SegmentoTiempo(
    private var inicio: LocalDateTime,
    private var fin: LocalDateTime,
    var esConjunta: Boolean,
    var puntuacion: Double
) {
    fun getInicio() = inicio
    fun getFin() = fin
}
""")

# AbstractActividad
with open(os.path.join(base_dir, "AbstractActividad.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

abstract class AbstractActividad(
    private val id: Int,
    var titulo: String,
    private var descripcion: String,
    var propietario: Usuario,
    private var esPrivada: Boolean,
    private var segmentoTiempo: SegmentoTiempo
)
""")

# ActividadSolitaria
with open(os.path.join(base_dir, "ActividadSolitaria.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class ActividadSolitaria(
    id: Int,
    titulo: String,
    descripcion: String,
    propietario: Usuario,
    esPrivada: Boolean,
    segmentoTiempo: SegmentoTiempo
) : AbstractActividad(id, titulo, descripcion, propietario, esPrivada, segmentoTiempo)
""")

# ActividadConjunta
with open(os.path.join(base_dir, "ActividadConjunta.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class ActividadConjunta(
    id: Int,
    titulo: String,
    descripcion: String,
    propietario: Usuario,
    esPrivada: Boolean,
    segmentoTiempo: SegmentoTiempo
) : AbstractActividad(id, titulo, descripcion, propietario, esPrivada, segmentoTiempo)
""")

# Conflicto
with open(os.path.join(base_dir, "Conflicto.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class Conflicto(
    var user: Usuario,
    var descripcion: String,
    var gravedad: Int
)
""")

# RecomendacionHorario
with open(os.path.join(base_dir, "RecomendacionHorario.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos

class RecomendacionHorario(
    var mejorSlot: SegmentoTiempo,
    var opcionesAlternativas: List<SegmentoTiempo>,
    var conflictos: List<Conflicto>
)
""")

