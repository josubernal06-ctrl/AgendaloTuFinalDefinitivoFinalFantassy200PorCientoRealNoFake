import os

file_path = "/home/josu/AndroidStudioProjects/AgendaloTuFinalDefinitivoFinalFantassy200RealNoFake/app/src/main/java/com/example/agendalotufinaldefinitivofinalfantassy200realnofake/modelos/Grupo.kt"

with open(file_path, "r") as f:
    content = f.read()

content = content.replace("var miembros", "var miebros")
content = content.replace("miembros is MutableList", "miebros is MutableList")
content = content.replace("miembros as MutableList", "miebros as MutableList")
content = content.replace("fun eliminarMiembro", "fun eleiminarMiembro")

with open(file_path, "w") as f:
    f.write(content)

