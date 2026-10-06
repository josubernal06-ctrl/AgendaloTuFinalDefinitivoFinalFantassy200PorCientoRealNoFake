import os
import shutil

base_dir = "/home/josu/AndroidStudioProjects/AgendaloTuFinalDefinitivoFinalFantassy200RealNoFake/app/src/main/java/com/example/agendalotufinaldefinitivofinalfantassy200realnofake"

# 1. Delete data/model and data/repository
shutil.rmtree(os.path.join(base_dir, "data"), ignore_errors=True)

# 2. Update imports in MainActivity and UserProfileScreen
for file_path in [os.path.join(base_dir, "MainActivity.kt"), os.path.join(base_dir, "ui/screens/UserProfileScreen.kt")]:
    with open(file_path, "r") as f:
        content = f.read()
    content = content.replace("data.model.Usuario", "modelos.Usuario")
    # Also replace usuario.nombre to usuario.nickname in UserProfileScreen
    if "UserProfileScreen.kt" in file_path:
        content = content.replace("usuario.nombre", "usuario.nickname")
        content = content.replace("nombre = \"Juan", "nickname = \"Juan")
    with open(file_path, "w") as f:
        f.write(content)

# 3. Create interfaces
interfaces_dir = os.path.join(base_dir, "interfaces")
os.makedirs(interfaces_dir, exist_ok=True)

with open(os.path.join(interfaces_dir, "IParticipante.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces

interface IParticipante {
    fun notificar(mensaje: String)
}
""")

with open(os.path.join(interfaces_dir, "IOrganizador.kt"), "w") as f:
    f.write("""package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.interfaces

import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.modelos.Usuario

interface IOrganizador {
    fun crearGrupo(nombre: String)
    fun invitarMiembro(user: Usuario)
}
""")

