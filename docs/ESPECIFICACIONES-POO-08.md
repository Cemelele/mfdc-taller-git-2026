# Especificaciones — POO-08 aplicado a Minecraft

**Ejercicio:** POO-08 — Arquitectura en capas, persistencia, reglas de negocio
e invariantes de ciclo de vida
**Asignatura:** Lenguaje de Programación 3 (CYT646) · Comisión CYT646 F
**Alumno:** MFDC — usuario de GitHub [Cemelele](https://github.com/Cemelele)
**Trabajo individual**
**Repositorio:** <https://github.com/Cemelele/mfdc-taller-git-2026/tree/main>
**Rama principal:** <https://github.com/Cemelele/mfdc-taller-git-2026/tree/main>

## 1. Objetivo

Publicar un servicio HTTP con Spring Boot sobre el modelado de **Minecraft**,
con una arquitectura de tres capas (dominio, negocio y HTTP), persistencia real
en H2, validaciones centralizadas en los services y reglas de ciclo de vida
irreversibles para las entidades. El proyecto debe poder clonarse, ejecutarse
con `./mvnw spring-boot:run` y probarse por HTTP o desde la colección de
Insomnia incluida.

## 2. Capas y responsabilidades

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Dominio | `py.edu.uc.lp3.domain` | Atributos privados, constructores y métodos simples que mutan estado. No lanza excepciones ni valida datos. |
| Persistencia | `py.edu.uc.lp3.repository` | `EntidadRepository` y `SimulacionExplosionRepository`, ambos Spring Data JPA. |
| Negocio | `py.edu.uc.lp3.service` | `HostilService` y `SimulacionService`: validaciones, reglas del juego, transacciones e invariantes. |
| HTTP | `py.edu.uc.lp3.rest.controller` | `IndexController`, `EntidadController`, `HostilController` y `SimulacionController`. |
| Contratos | `py.edu.uc.lp3.rest.dto` | `HostilDTO` y `SimulacionDTO` para desacoplar la API del modelo JPA. |
| Modelo de simulación | `py.edu.uc.lp3.model` | `SimulacionExplosion` y `ResultadoVictima`. |
| Errores | `py.edu.uc.lp3.exception` | Jerarquía de excepciones y `ManejadorExcepciones` global. |

El dominio incluye `Entidad` (raíz abstracta), `Hostil` (`@MappedSuperclass`),
`NoHostil`, `Jugador`, `Zombie`, `Creeper`, `Esqueleto`, `Enderman`, `Aldeano`,
`Cerdo`, `Hitbox`, `Electrificable` y `UICapable`.

## 3. Persistencia H2 y JPA

La configuración real de `src/main/resources/application.properties` usa H2 en
archivo:

```properties
spring.datasource.url=jdbc:h2:file:./data/entidades-db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

- La URL `jdbc:h2:file:./data/entidades-db` guarda la base en disco dentro de
  `./data/entidades-db`, no en memoria, por lo que los datos sobreviven al
  reinicio.
- `ddl-auto=update` crea o actualiza las tablas sin borrar las filas.
- `open-in-view=false` cierra la sesión JPA al terminar la transacción del
  service.
- La consola de H2 se publica en `http://localhost:8080/h2-console`.

### Herencia JOINED con discriminador

`Entidad` es la entidad raíz con `@Inheritance(strategy = JOINED)` y
`@DiscriminatorColumn(name = "tipo")`. La tabla `entidad` guarda los campos
comunes y la columna `tipo`. `Zombie` y `Creeper` son entidades persistidas con
su propia tabla y `@DiscriminatorValue`; sus claves primarias referencian
`entidad.id`. `Hostil` es `@MappedSuperclass` y no genera tabla. Hibernate usa
`tipo` para reconstruir la subclase correcta y hace `JOIN` por `id` para leer
sus columnas específicas.

## 4. Dominio puro

Las clases de `domain` no contienen `IllegalArgumentException` ni reglas de
negocio. Los constructores asignan el estado recibido y los mensajes como
`renombrar`, `ajustarVida`, `cambiarVelocidad`, `recibirDanio`, `morir`,
`cambiarRangoAggro` y `cambiarDanioAtaque` solo mutan atributos. Las
validaciones de datos se hacen antes de llamar al dominio, en los services.

`Creeper` conserva su constructor `Creeper(String nombre, int vida)`, que
delega en `super(...)`, y su estado de explosión (`radioExplosion`, `cargado`,
`encendido`). El cálculo de daño por distancia y la destrucción del Creeper se
resuelven en `SimulacionService`.

## 5. Reglas de negocio e invariantes

1. **Electrificación:** `Electrificable` define la capacidad de reaccionar a un
   rayo; `HostilService` electrifica un Creeper persistido y
   `SimulacionService` aplica la carga durante una explosión. Un Creeper
   cargado usa daño base 25 en lugar de 15, mientras que el Cerdo se
   transforma en Piglin Zombificado.
2. **Explosión:** el daño baja linealmente con la distancia y es cero fuera del
   radio de explosión. La explosión destruye siempre al Creeper.
3. **Ciclo de vida irreversible:** una entidad con `vida <= 0` no puede
   revivir. Cualquier `PUT` o `PATCH` sobre una entidad muerta, o cualquier
   intento de modificar su vida, responde `409 Conflict`.
4. **Explosión única:** un Creeper ya muerto o que ya explotó no puede volver a
   explotar. Un segundo intento de simulación responde `409 Conflict`.
5. **Validaciones de datos:** nombre no vacío, vida positiva, velocidad no
   negativa, rango de aggro positivo, daño de ataque positivo y distancia no
   negativa. Los errores de datos responden `400 Bad Request`.

## 6. API REST

| Método y ruta | Controller | Descripción |
|---|---|---|
| `GET /` | `IndexController` | Estado del servicio |
| `GET /entidad/{tipo}` | `EntidadController` | Construcción de entidades de demostración |
| `GET /entidades` | `EntidadController` | Listado de entidades de demostración |
| `GET /rayo/{tipo}` | `EntidadController` | Mensajes de `Electrificable` |
| `GET /ui/{tipo}` | `EntidadController` | Datos de `UICapable` |
| `GET /api/hostiles` | `HostilController` | Listar hostiles (`?nombre=` opcional) |
| `POST /api/hostiles` | `HostilController` | Crear `zombie` o `creeper` |
| `GET /api/hostiles/{id}` | `HostilController` | Buscar hostil |
| `PUT /api/hostiles/{id}` | `HostilController` | Reemplazar el recurso |
| `PATCH /api/hostiles/{id}` | `HostilController` | Actualización parcial |
| `DELETE /api/hostiles/{id}` | `HostilController` | Eliminar hostil |
| `GET /api/hostiles/{id}/rayo` | `HostilController` | Electrificar Creeper persistido |
| `POST /api/simulaciones/explosion` | `SimulacionController` | Simular y persistir explosión |
| `GET /api/simulaciones` | `SimulacionController` | Listar simulaciones |
| `GET /api/simulaciones/{id}` | `SimulacionController` | Buscar simulación |

## 7. Manejo de errores y códigos HTTP

`ManejadorExcepciones` (`@RestControllerAdvice`) devuelve un JSON uniforme:

```json
{
  "status": 409,
  "error": "Conflict",
  "tipo": "EstadoInvalidoException",
  "mensaje": "La entidad 7 esta muerta (vida <= 0) y no puede revivir ni modificarse"
}
```

| Excepción | HTTP | Caso |
|---|---|---|
| `EntidadInvalidaException` | `400 Bad Request` | Datos inválidos o incompletos al crear o actualizar un hostil |
| `SimulacionInvalidaException` | `400 Bad Request` | Petición de simulación inválida, víctimas faltantes o distancia negativa |
| `EntidadNoEncontradaException` | `404 Not Found` | ID de entidad inexistente |
| `CreeperNoEncontradoException` | `404 Not Found` | ID de Creeper inexistente en una simulación |
| `EstadoInvalidoException` | `409 Conflict` | Entidad muerta que se intenta revivir o modificar; Creeper muerto o ya explotado que se intenta explotar de nuevo |
| `HostilDomainException` | `400 Bad Request` | Red de seguridad para cualquier excepción nueva de la familia |

## 8. Pruebas

```bash
git clone https://github.com/Cemelele/mfdc-taller-git-2026.git
cd mfdc-taller-git-2026
./mvnw test
./mvnw spring-boot:run
```

La suite ejecuta **44 tests**:

- `ApplicationTests`: 1
- `domain/EntidadTest`: 13
- `repository/EntidadRepositoryTest`: 2
- `service/HostilServiceTest`: 4
- `service/SimulacionServiceTest`: 6
- `rest/controller/EntidadControllerTest`: 12
- `rest/controller/ManejadorExcepcionesTest`: 6

Incluye pruebas específicas de `409 Conflict` al intentar revivir un mob con
`vida = 0` y al intentar explotar dos veces un Creeper ya destruido.

## 9. Colección de Insomnia

La colección verificada está en
[`docs/insomnia/insomnia_crud_hostil.json`](insomnia/insomnia_crud_hostil.json).
Para usarla: iniciar el servicio, abrir Insomnia, importar el archivo desde
**Application → Import/Export → Import Data → From File**, seleccionar el
workspace y ejecutar las solicitudes con **Send**. Las URLs apuntan a
`http://localhost:8080`; conviene crear primero un Zombie y un Creeper, luego
probar el CRUD y finalmente la simulación de explosión.
