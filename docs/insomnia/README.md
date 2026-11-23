# Colección de Insomnia (POO-09)

Importar `poo-09-insomnia.yaml` con Insomnia 11 o superior (formato v5). Trae el
entorno con las tres URLs: a1_url (mi servicio, :8080), ruth_url (A2, :8081) y
seb_url (A3, :8082).

## Orden de uso
1. Levantar los tres servicios (./mvnw spring-boot:run; Ruth y Sebastián con
   SERVER_PORT=8081 y 8082).
2. Crear mobs nuevos con las carpetas "Ruth" y "Sebastián" y copiar sus ids a las
   variables del entorno. Los ids del archivo son de una corrida anterior y esos
   mobs ya están consumidos.
3. Ejecutar en A1: escenario feliz, rechazo de A2, rechazo de A3 y el GET final.

Cada explosión consume Creepers (y la de Ruth mata a otros mobs por reacción en
cadena), así que para repetir una prueba hay que crear mobs nuevos.

`respuestas/` guarda un ejemplo de cada caso.