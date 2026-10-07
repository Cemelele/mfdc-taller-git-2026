# Especificaciones — POO-06 aplicado a Minecraft

**Ejercicio:** POO-06 — Revisión, paquetes, constructores y sobrecarga
**Asignatura:** Lenguaje de Programación 3 (CYT646) · Comisión CYT646 F
**Alumno:** MFDC — usuario de GitHub [Cemelele](https://github.com/Cemelele)
**Trabajo individual**
**Repositorio:** <https://github.com/Cemelele/mfdc-taller-git-2026>
**Commit de la solución:** <https://github.com/Cemelele/mfdc-taller-git-2026/commit/b42ec31dd0133acf0f5fa062203fec8141e0aeea>

## 1. Objetivo

Publicar un servicio HTTP con Spring Boot sobre el modelado de **Minecraft**
elegido en septiembre, donde se vean herencia, **sobreescritura**,
ocultamiento de la información, paquetes con sentido, constructores simples y
sobrecargados, y **sobrecarga** de al menos un mensaje del dominio. Un
compañero debe poder clonar el repositorio, arrancar el servicio con
`./mvnw spring-boot:run` y usarlo por HTTP, sin abrir un `main()` en el IDE.

## 2. Consignas aplicadas a este dominio

| # | Consigna del enunciado | Cómo quedó en este repo |
|---|---|---|
| 1 | Paquetes según el template | `py.edu.uc.lp3.Application` (arranque), `py.edu.uc.lp3.domain` (todo el modelado), `py.edu.uc.lp3.rest.controller` (los dos servicios REST) |
| 2 | El modelado de septiembre compila dentro del Spring Boot | `Entidad` (abstracta), `Hostil` y `NoHostil` (abstractas), `Hitbox`, `Jugador`, `Zombie`, `Creeper`, `Esqueleto`, `Enderman`, `Aldeano`, `Cerdo` |
| 3 | Método abstracto en la base + dos hijas que lo sobreescriben | `Entidad.comportamiento(Jugador)` es abstracto; lo sobreescriben `Hostil`, `NoHostil` y `Jugador`, misma firma, cada una a su modo |
| 4 | Dos servicios REST: `GET /` y controller que construye desde la URL | `IndexController` en `GET /`; `EntidadController` en `GET /entidad/{tipo}` y `GET /entidades`, que construye con los parámetros de la URL |
| 5 | JSON con la respuesta del método abstracto, hablando por el tipo padre | El JSON trae `comportamiento`, escrito por la clase hija sobreescrita; el controller recibe un `Entidad` y nunca pregunta el tipo concreto |
| 6 | Constructores simples y sobrecargados; la hija llama a `super` | `Zombie()`, `Zombie(int)`, `Zombie(String,int)` · `Jugador()` (Steve), `Jugador(String)` · `Aldeano()` (Granjero), `Aldeano(String)`; todos delegan con `this(...)` y llaman a `super(...)` |
| 7 | Sobrecarga de un mensaje del dominio | `Entidad.avanzar()` / `avanzar(double)` y `Entidad.recibirDanio(int)` / `recibirDanio(int, String)`; ambas se eligen desde la URL (`?distancia=`, `?atacante=`) |
| 8 | README con licencia, Mermaid y apartado de sobrecarga/sobreescritura | `README.md` en la raíz |
| 9 | Bitácora de IA | `BITACORA.md` en la raíz (marca: OpenCode; modelo: `opencode/big-pickle`; resumen de prompts) |

### Vocabulario del dominio

Una **entidad** es cualquier ser del mundo (jugador, mob o animal). Un **mob**
es una criatura que no es el jugador. El **Creeper** es el mob verde que se
acerca y explota. Las reglas de vida, movimiento y muerte viven en la
jerarquía de clases, no en el controller.

## 3. Reglas de diseño que sostiene el modelo

- **Ocultamiento:** todos los campos de `Entidad` son `private` y no hay
  setters. El estado cambia solo por constructores y mensajes de la clase.
- **Invariante:** la vida nunca es negativa; daño negativo, nombres en blanco
  o distancias negativas se rechazan en el constructor o en el mensaje, antes
  de tocar el estado.
- **Herencia de comportamiento:** `comportamiento()` es el mensaje abstracto;
  cada hija decide su implementación y, si hace falta, llama a `super`.
- **REST no es el modelo:** el controller solo construye y pide mensajes;
  si la URL trae un valor ilegal, la clase lanza `IllegalArgumentException` y
  el controller lo devuelve como HTTP 400 sin haber modificado el objeto.

## 4. Cómo probarlo

```bash
git clone https://github.com/Cemelele/mfdc-taller-git-2026.git
cd mfdc-taller-git-2026
./mvnw test                # 17 tests: dominio + HTTP (MockMvc)
./mvnw spring-boot:run     # levanta en http://localhost:8080 (JDK 21)
```

En otra terminal:

```bash
curl -s localhost:8080/
# Bienvenido al taller de Git 2026 - LP3. Dominio: Minecraft. ...

# Construye desde la URL: constructor sobrecargado (?vida=35),
# sobrecarga de recibirDanio (?atacante=) y de avanzar (?distancia=)
curl -s "localhost:8080/entidad/zombie?vida=35&danio=10&atacante=Creeper"
# {"entidad":{...},"comportamiento":"Zombie golpea cuerpo a cuerpo a Steve. ...",
#  "avanzar":"Zombie avanza 2.0 bloques.","recibirDanio":"Creeper ataca a Zombie. ...",
#  "vidaFinal":25,"viva":true}

# Constructor simple por defecto (Aldeano = Granjero)
curl -s "localhost:8080/entidad/aldeano?profesion="

# Todas las hijas, cada una con su texto sobreescrito
curl -s localhost:8080/entidades

# La clase rechaza un valor ilegal: HTTP 400 con el motivo
curl -i "localhost:8080/entidad/creeper?danio=-5"
```

Qué mirar:

1. `GET /` responde que el servicio está vivo.
2. En el JSON, `comportamiento` cambia según el tipo de entidad: ahí está la
   **sobreescritura** (mismo mensaje `comportamiento(Jugador)`, distinta
   implementación).
3. `?vida=35` construye con `Zombie(int)` y `?profesion=` con `Aldeano()`:
   ahí están los **constructores sobrecargados**.
4. `?atacante=Creeper` cambia la firma de `recibirDanio` que se ejecuta:
   ahí está la **sobrecarga de mensajes**.
5. `?danio=-5` devuelve `400 {"error":"El dano no puede ser negativo"}`: la
   regla vive en la clase, no en el controller.

## 5. Pregunta de anclaje

*¿Qué ocurre si el controller asigna a mano la vida o la munición?*

No puede hacerlo: `vida` es `private`, `Entidad` no tiene setters y el
controller solo conoce la API de mensajes del tipo padre. Si intentara algo
como `entidad.setVida(0)` el proyecto no compilaría; y si manda un valor
ilegal por la URL, la clase lanza `IllegalArgumentException` y el controller
responde HTTP 400 sin dejar el objeto en un estado imposible.
