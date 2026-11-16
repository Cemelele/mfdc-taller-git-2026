# mfdc-taller-git-2026 — POO-08

Servicio HTTP con **Spring Boot** sobre el dominio de **Minecraft** para el
ejercicio **POO-08 — Arquitectura en capas, persistencia, reglas de negocio e
invariantes de ciclo de vida**, de Lenguaje de Programación 3 (CYT646).

- **Alumno:** MFDC
- **Usuario de GitHub:** [Cemelele](https://github.com/Cemelele)
- **Comisión:** CYT646 F — trabajo individual
- **Repositorio:** <https://github.com/Cemelele/mfdc-taller-git-2026/tree/main>
- **Guía del taller:** [docs/TALLER_GIT.md](docs/TALLER_GIT.md)
- **Especificaciones del dominio:** [docs/ESPECIFICACIONES-POO-08.md](docs/ESPECIFICACIONES-POO-08.md)
- **Bitácora de uso de IA:** [BITACORA.md](BITACORA.md)
- **Colección de Insomnia:** [docs/insomnia/insomnia_crud_hostil.json](docs/insomnia/insomnia_crud_hostil.json)

## Estado actual de la entrega

- El paquete `py.edu.uc.lp3.domain` es una capa de dominio **pura**: las
  entidades solo guardan estado y lo mutan con métodos simples; no lanzan
  excepciones ni contienen validaciones.
- Las validaciones de datos, las reglas del juego y las invariantes de ciclo
  de vida viven en `HostilService` y `SimulacionService`.
- La persistencia usa **H2 en archivo** (`jdbc:h2:file:./data/entidades-db`) y
  Spring Data JPA con herencia `JOINED` y columna discriminadora `tipo`.
- La API responde con un JSON uniforme para errores **400**, **404** y
  **409 Conflict**.
- La suite actual ejecuta **44 tests** con `./mvnw test`.

## Enlace de la solución

Código, tests y documentación de la rama principal:

<https://github.com/Cemelele/mfdc-taller-git-2026/tree/main>

## Requisitos y ejecución

Requisitos: **JDK 21**.

```bash
./mvnw spring-boot:run
```

El servicio queda en `http://localhost:8080`. Para detenerlo: `Ctrl+C`.

La consola web de H2 queda disponible en `http://localhost:8080/h2-console`
con la URL JDBC `jdbc:h2:file:./data/entidades-db`, usuario `sa` y contraseña
vacía.

## Configuración de H2 y JPA

Toda la configuración está en `src/main/resources/application.properties`.
Cada propiedad cumple una función concreta:

| Propiedad | Qué hace |
|---|---|
| `spring.application.name=mfdc-taller-git-2026` | Define el nombre lógico de la aplicación Spring Boot que aparece en logs y en la consola de administración. |
| `spring.datasource.url=jdbc:h2:file:./data/entidades-db` | Configura H2 con **persistencia en archivo** dentro de `./data/entidades-db`, por lo que los datos sobreviven al reinicio. |
| `spring.datasource.driverClassName=org.h2.Driver` | Indica el driver JDBC de H2 que usa el pool de conexiones. |
| `spring.datasource.username=sa` | Establece el usuario de conexión a la base H2. |
| `spring.datasource.password=` | Deja la contraseña vacía, válida para el entorno local de desarrollo. |
| `spring.jpa.hibernate.ddl-auto=update` | Hace que Hibernate cree las tablas faltantes y actualice el esquema sin borrar las filas existentes. |
| `spring.jpa.open-in-view=false` | Desactiva Open Session in View: la sesión de JPA se cierra al terminar la transacción del service/repository y no queda abierta durante el render HTTP. |
| `spring.h2.console.enabled=true` | Habilita la consola web de H2 para inspeccionar las tablas en desarrollo. |
| `spring.h2.console.path=/h2-console` | Publica la consola web de H2 en la ruta `/h2-console`. |

Los tests con `@DataJpaTest` reemplazan automáticamente esta configuración por
una base H2 embebida en memoria, de modo que no ensucian el archivo real de
`./data/entidades-db`.

## Dependencias principales (`pom.xml`)

| Dependencia | Uso en el proyecto |
|---|---|
| `spring-boot-starter-web` | API REST, `@RestController`, Jackson y servidor embebido. |
| `spring-boot-starter-data-jpa` | Repositorios Spring Data, Hibernate y transacciones. |
| `spring-boot-starter-validation` | Soporte de Bean Validation en el classpath de la aplicación. |
| `com.h2database:h2` | Base de datos H2 en archivo para desarrollo y ejecución local. |
| `spring-boot-starter-test` | JUnit 5, MockMvc, AssertJ y utilidades de test de Spring Boot. |

El proyecto usa Spring Boot `3.5.16`, Java `21` y el wrapper de Maven incluido
en el repositorio.

## Arquitectura y paquetes

```text
src/main/java/py/edu/uc/lp3/
├── Application.java                         arranque de Spring Boot
├── domain/                                  dominio puro (sin HTTP ni JPA de negocio)
│   ├── Entidad.java                         raíz abstracta de la jerarquía
│   ├── Hostil.java                          @MappedSuperclass de mobs hostiles
│   ├── NoHostil.java                        mobs pasivos
│   ├── Jugador.java                         entidad jugador
│   ├── Zombie.java  Creeper.java            hijas persistidas de Hostil
│   ├── Esqueleto.java  Enderman.java        otras entidades hostiles
│   ├── Aldeano.java  Cerdo.java             entidades pasivas
│   ├── Hitbox.java                          valor embebido (ancho x alto)
│   ├── Electrificable.java                  capacidad de reaccionar a un rayo
│   └── UICapable.java                       contrato de datos para dibujar
├── repository/                              acceso a datos con Spring Data JPA
│   ├── EntidadRepository.java               JpaRepository<Entidad, Long>
│   └── SimulacionExplosionRepository.java   JpaRepository<SimulacionExplosion, Long>
├── service/                                 capa de negocio y validaciones
│   ├── HostilService.java                   CRUD, validaciones y ciclo de vida
│   └── SimulacionService.java               explosión, daño y electrificación
├── rest/controller/                         capa HTTP
│   ├── IndexController.java                 GET /
│   ├── EntidadController.java               endpoints de demostración del dominio
│   ├── HostilController.java                CRUD /api/hostiles
│   └── SimulacionController.java            /api/simulaciones
├── rest/dto/                                contratos de entrada y salida
│   ├── HostilDTO.java
│   └── SimulacionDTO.java
├── model/                                   entidades persistidas de simulación
│   ├── SimulacionExplosion.java
│   └── ResultadoVictima.java
└── exception/                               jerarquía de errores de dominio
    ├── HostilDomainException.java           base de todas las excepciones
    ├── EntidadException.java                base de errores de entidad
    ├── EntidadInvalidaException.java        400 Bad Request
    ├── EntidadNoEncontradaException.java    404 Not Found
    ├── CreeperNoEncontradoException.java    404 Not Found
    ├── SimulacionInvalidaException.java     400 Bad Request
    ├── EstadoInvalidoException.java         409 Conflict
    └── ManejadorExcepciones.java            @RestControllerAdvice
```

## Diagrama de arquitectura

```mermaid
classDiagram
    class Entidad {
        <<abstract>>
        -Long id
        -int vida
        -String nombre
        -Hitbox hitboxFisico
        -String drop
        -String imagen
        -String sonido
        -double velocidad
        -double x
        -double y
        -double z
        -double rotacion
        +comportamiento(Jugador) String
        +avanzar(double) String
        +recibirDanio(int) String
        +recibirDanio(int, String) String
        +renombrar(String) String
        +ajustarVida(int) String
        +cambiarVelocidad(double) String
        +morir() String
        +estaMuerto() boolean
    }

    class Hostil {
        <<abstract, MappedSuperclass>>
        -double rangoAggro
        -int danioAtaque
        +accionAtaque(Jugador) String
    }

    class NoHostil {
        <<abstract>>
        +interactuar(Jugador) String
    }

    class Jugador
    class Zombie
    class Creeper
    class Esqueleto
    class Enderman
    class Aldeano
    class Cerdo
    class Hitbox

    class Electrificable {
        <<interface>>
        +recibirRayo() String
        +avisoRayo() String
    }

    class UICapable {
        <<interface>>
        +getImagen() String
        +getPosicion() String
        +getSonido() String
        +getVelocidad() double
    }

    class EntidadRepository {
        <<interface, JpaRepository>>
        +save(Entidad) Entidad
        +findById(Long) Optional~Entidad~
        +findAll() List~Entidad~
        +findByNombreContainingIgnoreCase(String) List~Entidad~
        +findByVidaGreaterThan(int) List~Entidad~
    }

    class SimulacionExplosionRepository {
        <<interface, JpaRepository>>
        +save(SimulacionExplosion) SimulacionExplosion
        +findById(Long) Optional~SimulacionExplosion~
        +findAll() List~SimulacionExplosion~
    }

    class HostilService {
        +crear(HostilRequest) HostilResponse
        +listar(String) List~HostilResponse~
        +buscar(Long) HostilResponse
        +actualizarParcial(Long, HostilRequest) HostilResponse
        +actualizarTotal(Long, HostilRequest) HostilResponse
        +eliminar(Long) void
        +recibirRayo(Long) Map
    }

    class SimulacionService {
        +simularExplosion(ExplosionRequest) ExplosionResultado
        +listar() List~SimulacionExplosion~
        +buscar(Long) SimulacionExplosion
    }

    class HostilController {
        <<RestController>>
        +crear(HostilRequest) ResponseEntity
        +listar(String) List~HostilResponse~
        +buscar(Long) HostilResponse
        +actualizarTotal(Long, HostilRequest) HostilResponse
        +actualizarParcial(Long, HostilRequest) HostilResponse
        +eliminar(Long) ResponseEntity
        +rayo(Long) Map
    }

    class SimulacionController {
        <<RestController>>
        +explosion(ExplosionRequest) ResponseEntity
        +listar() List~SimulacionExplosion~
        +buscar(Long) SimulacionExplosion
    }

    class EntidadController {
        <<RestController>>
        +crear(String, String, int, String, String, String, double, int) Map
        +todas() List~Map~
        +rayo(String) Map
        +ui(String) Map
    }

    class IndexController {
        <<RestController>>
        +index() String
    }

    class SimulacionExplosion {
        -Long id
        -Long creeperId
        -boolean creeperCargado
        -boolean creeperEncendido
        -boolean creeperDestruido
        -double radio
        -int danioBase
        +agregarResultado(ResultadoVictima) void
        +marcarCreeperDestruido() void
    }

    class ResultadoVictima {
        -Long hostilId
        -String tipo
        -double distancia
        -int danio
        -int vidaAntes
        -int vidaDespues
        -boolean viva
    }

    class HostilDomainException {
        <<RuntimeException>>
    }
    class EntidadException
    class EntidadInvalidaException
    class EntidadNoEncontradaException
    class CreeperNoEncontradoException
    class SimulacionInvalidaException
    class EstadoInvalidoException
    class ManejadorExcepciones {
        <<RestControllerAdvice>>
    }

    Entidad <|-- Hostil
    Entidad <|-- NoHostil
    Entidad <|-- Jugador
    Hostil <|-- Zombie
    Hostil <|-- Creeper
    Hostil <|-- Esqueleto
    Hostil <|-- Enderman
    NoHostil <|-- Aldeano
    NoHostil <|-- Cerdo
    Entidad *-- Hitbox
    Entidad ..|> UICapable
    Creeper ..|> Electrificable
    Cerdo ..|> Electrificable

    EntidadRepository ..> Entidad : persiste
    SimulacionExplosionRepository ..> SimulacionExplosion : persiste
    SimulacionExplosion *-- ResultadoVictima

    HostilService ..> EntidadRepository : usa
    HostilService ..> Hostil : construye y valida
    HostilService ..> EntidadInvalidaException : lanza
    HostilService ..> EntidadNoEncontradaException : lanza
    HostilService ..> EstadoInvalidoException : lanza

    SimulacionService ..> EntidadRepository : usa
    SimulacionService ..> SimulacionExplosionRepository : usa
    SimulacionService ..> Creeper : daña y destruye
    SimulacionService ..> SimulacionInvalidaException : lanza
    SimulacionService ..> CreeperNoEncontradoException : lanza
    SimulacionService ..> EstadoInvalidoException : lanza

    HostilController ..> HostilService : delega
    SimulacionController ..> SimulacionService : delega
    EntidadController ..> Entidad : construye
    EntidadController ..> Electrificable : usa interfaz
    EntidadController ..> UICapable : usa interfaz

    HostilDomainException <|-- EntidadException
    EntidadException <|-- EntidadInvalidaException
    EntidadException <|-- EntidadNoEncontradaException
    EntidadNoEncontradaException <|-- CreeperNoEncontradoException
    EntidadException <|-- SimulacionInvalidaException
    HostilDomainException <|-- EstadoInvalidoException
    ManejadorExcepciones ..> HostilDomainException : traduce a HTTP
```

## Reglas del juego

- **Electrificación mediante `Electrificable`:** cuando un rayo alcanza a un
  `Creeper`, el service lo marca como cargado y su explosión pasa de 15 a 25
  puntos de daño base; el `Cerdo` implementa la misma capacidad y se
  transforma en Piglin Zombificado.
- **Simulación de explosión con ciclo de vida irreversible:** el daño de la
  explosión baja linealmente con la distancia y solo aplica dentro del radio,
  y el `Creeper` que detona muere (`vida = 0`) y no puede volver a explotar ni
  revivir, por lo que un segundo intento responde `409 Conflict`.

## Endpoints

| Método y ruta | Controller | Qué hace |
|---|---|---|
| `GET /` | `IndexController` | Confirma que el servicio está vivo |
| `GET /entidad/{tipo}` | `EntidadController` | Construye una entidad de demostración desde la URL |
| `GET /entidades` | `EntidadController` | Lista las entidades de demostración con su comportamiento |
| `GET /rayo/{tipo}` | `EntidadController` | Pide los mensajes de `Electrificable` |
| `GET /ui/{tipo}` | `EntidadController` | Devuelve imagen, posición, sonido y velocidad (`UICapable`) |
| `GET /api/hostiles` | `HostilController` | Lista hostiles persistidos; acepta `?nombre=` |
| `POST /api/hostiles` | `HostilController` | Crea un `zombie` o un `creeper` |
| `GET /api/hostiles/{id}` | `HostilController` | Busca un hostil por ID |
| `PUT /api/hostiles/{id}` | `HostilController` | Reemplaza nombre, vida, velocidad y atributos opcionales |
| `PATCH /api/hostiles/{id}` | `HostilController` | Actualiza solo los campos enviados |
| `DELETE /api/hostiles/{id}` | `HostilController` | Elimina un hostil |
| `GET /api/hostiles/{id}/rayo` | `HostilController` | Electrifica un Creeper persistido y devuelve el resultado |
| `POST /api/simulaciones/explosion` | `SimulacionController` | Simula y persiste una explosión de Creeper |
| `GET /api/simulaciones` | `SimulacionController` | Lista las simulaciones persistidas |
| `GET /api/simulaciones/{id}` | `SimulacionController` | Busca una simulación por ID |

Tipos válidos en `EntidadController`: `creeper`, `zombie`, `esqueleto`,
`enderman`, `cerdo`, `aldeano`. En `/rayo/{tipo}` responden `creeper` y
`cerdo`; en el CRUD persistido solo se aceptan `zombie` y `creeper`.

Ejemplos:

```bash
curl "localhost:8080/entidad/zombie?vida=35&danio=10&atacante=Creeper"
curl "localhost:8080/rayo/cerdo"
curl -i "localhost:8080/entidad/creeper?danio=-5"

curl -X POST "localhost:8080/api/hostiles" \
  -H "Content-Type: application/json" \
  -d '{"tipo":"creeper","nombre":"Boom","vida":20,"velocidad":0.25}'

curl -X POST "localhost:8080/api/simulaciones/explosion" \
  -H "Content-Type: application/json" \
  -d '{"creeperId":1,"cargado":true,"victimas":[{"hostilId":2,"distancia":1.5}]}'
```

`POST /api/simulaciones/explosion` devuelve `201 Created`; si la petición está
mal formada devuelve `400`; si el Creeper no existe, `404`; y si ya está muerto
o ya explotó, `409 Conflict`.

## Qué hay que pensar

### ¿Por qué las reglas van en el service y no en la entity?

Porque la entidad debe ser un objeto de dominio reutilizable y fácil de probar:
solo conserva atributos privados, constructores y métodos simples que mutan su
estado. Las validaciones de entrada, la decisión de aceptar o rechazar una
operación, el acceso al repositorio y la transacción pertenecen al service. Una
regla como “un mob muerto no revive” no depende solo del objeto, sino también
de la operación solicitada (`PUT`/`PATCH`) y del estado persistido; por eso se
controla en `HostilService` antes de mutar. La simulación de explosión
involucra varias entidades, distancias, daño y persistencia, así que vive en
`SimulacionService`. Así el dominio no lanza excepciones ni conoce HTTP, y las
validaciones están en un único lugar para todos los endpoints.

### ¿Cómo funciona la herencia JOINED con discriminador?

`Entidad` es la entidad raíz JPA (`@Entity`) y define
`@Inheritance(strategy = JOINED)` junto con
`@DiscriminatorColumn(name = "tipo")`. La tabla `entidad` guarda los campos
comunes y la columna `tipo`. Las clases persistidas hijas, `Zombie` y
`Creeper`, usan `@DiscriminatorValue` y tienen su propia tabla (`zombie` o
`creeper`) cuya clave primaria es también clave foránea hacia `entidad.id`.
Al leer, Hibernate usa `tipo` para saber qué subclase instanciar y hace un
`JOIN` por `id` para recuperar las columnas específicas. `Hostil` no genera
tabla porque es `@MappedSuperclass`: solo aporta campos y comportamiento a sus
hijas. `EntidadRepository` trabaja con el tipo padre y aun así devuelve
instancias concretas.

### ¿Cómo se manejan los códigos HTTP 400, 404 y 409?

`ManejadorExcepciones` es un `@RestControllerAdvice` que traduce cada
excepción de dominio a un JSON uniforme con `status`, `error`, `tipo` y
`mensaje`:

- **400 Bad Request:** la petición trae datos inválidos o incompletos
  (`EntidadInvalidaException`, `SimulacionInvalidaException`), por ejemplo
  nombre vacío, vida no positiva, velocidad negativa o distancia negativa.
- **404 Not Found:** el recurso pedido no existe
  (`EntidadNoEncontradaException`, `CreeperNoEncontradoException`).
- **409 Conflict:** la petición es válida, pero el estado actual no permite la
  operación (`EstadoInvalidoException`), por ejemplo intentar revivir una
  entidad con `vida <= 0` o explotar de nuevo un Creeper ya destruido.

## Importar y ejecutar la colección de Insomnia

1. Levantá la aplicación con `./mvnw spring-boot:run` y esperá a que escuche en
   `http://localhost:8080`.
2. Abrí Insomnia.
3. Entrá a **Application → Import/Export → Import Data → From File** (en
   algunas versiones aparece como **Import → From File**).
4. Seleccioná `docs/insomnia/insomnia_crud_hostil.json` desde la raíz del
   repositorio.
5. Elegí el workspace/proyecto destino y confirmá la importación; Insomnia
   detecta el formato del archivo y crea las solicitudes.
6. Verificá que las URLs de las solicitudes apunten a `http://localhost:8080`
   y ejecutá cada una con **Send**, en orden: primero crear un `zombie` y un
   `creeper`, después listar, consultar, actualizar y finalmente simular la
   explosión.
7. Repetí la simulación sobre el mismo Creeper para comprobar el
   `409 Conflict` del ciclo de vida irreversible.

## Tests

```bash
./mvnw test
```

La suite suma **44 tests**:

| Test | Cantidad |
|---|---:|
| `ApplicationTests` | 1 |
| `domain/EntidadTest` | 13 |
| `repository/EntidadRepositoryTest` | 2 |
| `service/HostilServiceTest` | 4 |
| `service/SimulacionServiceTest` | 6 |
| `rest/controller/EntidadControllerTest` | 12 |
| `rest/controller/ManejadorExcepcionesTest` | 6 |
| **Total** | **44** |

## Licencia

[Apache License 2.0](LICENSE): se puede usar, modificar y distribuir (incluso
comercialmente) acreditando el origen y sin garantías.
