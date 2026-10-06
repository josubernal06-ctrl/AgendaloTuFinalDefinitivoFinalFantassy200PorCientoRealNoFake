import os

file_path = "/home/josu/AndroidStudioProjects/AgendaloTuFinalDefinitivoFinalFantassy200RealNoFake/app/src/main/java/com/example/agendalotufinaldefinitivofinalfantassy200realnofake/ui/screens/UserProfileScreen.kt"

with open(file_path, "r") as f:
    content = f.read()

content = content.replace('valor = "#${usuario.id}",', 'valor = "#Oculto",')
content = content.replace('Usuario(id = 1, nickname =', 'Usuario(1, nickname =')

with open(file_path, "w") as f:
    f.write(content)

