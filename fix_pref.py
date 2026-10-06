import os

file_path = "/home/josu/AndroidStudioProjects/AgendaloTuFinalDefinitivoFinalFantassy200RealNoFake/app/src/main/java/com/example/agendalotufinaldefinitivofinalfantassy200realnofake/modelos/Pref_Horario.kt"

with open(file_path, "r") as f:
    content = f.read()

content = content.replace("var horaFin:", "var horaFIn:")

with open(file_path, "w") as f:
    f.write(content)

