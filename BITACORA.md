# Bitácora de uso de inteligencia artificial

Ejercicio **POO-06** — Revisión, paquetes, constructores y sobrecarga
Repositorio: `Cemelele/mfdc-taller-git-2026`
Dominio: **Minecraft**

## Herramienta y modelo

| Dato | Valor |
|---|---|
| **Marca / agente** | OpenCode (asistente de código en terminal) |
| **Modelo LLM exacto** | `opencode/big-pickle` |
| **Fecha** | 7 de octubre de 2026 |

## Resumen de los prompts usados

Los prompts fueron en español y, en frases propias, fueron estos:

1. **Análisis inicial del repositorio.** Pedí que lea la guía del taller
   (`docs/TALLER_GIT.md`) y el repositorio, y que diga qué falta para cerrar
   la Definition of Done (el enlace de Classroom no era accesible sin inicio
   de sesión, así que el análisis se hizo contra la guía del repo).
2. **Entrega del enunciado.** Pegué el enunciado completo de POO-06 con sus
   tres enlaces (enunciado, rúbrica y template de paquetes) para que verifique
   contra esos documentos, no contra suposiciones.
3. **Reestructuración y corrección.** La instrucción fue completar la tarea:
   mover las clases al template `py.edu.uc.lp3` (`domain`, `rest.controller`),
   arreglar los errores de compilación, y agregar lo que pedía la consigna
   (constructores sobrecargados, sobrecarga de mensajes del dominio, uso de
   ambos desde la URL).
4. **Verificación.** Pedí que verifique con `./mvnw test` y levantando el
   servicio real (`./mvnw spring-boot:run`) probando los endpoints con `curl`,
   incluyendo los casos de error (400 y 404).
5. **Documentación.** Pedí escribir el README (diagrama Mermaid alineado con
   `src/`, apartado de sobrecarga y sobreescritura, licencia), esta bitácora y
   las especificaciones del ejercicio para entregar en Classroom.

## Qué hizo el agente con cada prompt

- Leyó el repositorio, la guía del taller, el enunciado de POO-06, la rúbrica
  y el template de paquetes.
- Editó y creó archivos de código Java (`domain`, `rest.controller`, tests) y
  documentación Markdown.
- Ejecutó comandos de verificación: `./mvnw compile`, `./mvnw test` (17 tests,
  0 fallos) y `./mvnw spring-boot:run` con pruebas `curl` contra
  `http://localhost:8080`.
- No corrigió la calificación ni realizó la entrega en Classroom: el enlace al
  commit y el archivo de especificaciones los arma y entrega el alumno.

## Segunda sesión: CRUD REST con persistencia (H2)

| Dato                  | Valor                                      |
|-----------------------|--------------------------------------------|
| **Marca / agente**    | OpenCode (asistente de código en terminal) |
| **Modelo LLM exacto** | (completar con el modelo usado ese día)    |
| **Fecha**             | 28 de octubre de 2026                      |

### Prompt usado

El prompt lo dio el profesor en clase: crear un CRUD REST para la clase de
entidades que respete la arquitectura de 3 capas, con una capa de negocio
(service) con validaciones, persistiendo en H2 con Spring Data.

### Qué generó el agente

Creó la entidad JPA `EntidadPersistida`, el repository, el service y el
`EntidadPersistidaController` bajo `/api/entidades`, además de las dependencias
de Spring Data JPA y H2. El agente se quedó sin usos gratuitos antes de
terminar el prompt, por lo que completé y revisé el resto a mano.

### Qué corregí o decidí yo

- **Persistencia:** la configuración generada usaba una base en memoria
  (`jdbc:h2:mem`) con `ddl-auto=create-drop`, que borra los datos al apagar la
  aplicación. La cambié a una base en archivo (`jdbc:h2:file:./data/entidades-db`)
  con `ddl-auto=update` y agregué `data/` al `.gitignore`.
- **Service:** las mismas validaciones (nombre, vida, velocidad) estaban
  repetidas en varios lugares. Las unifiqué en un solo método y agregué una
  regla de negocio: solo se aceptan los tipos válidos del dominio.
- **Pruebas:** verifiqué que un tipo inválido devuelve 400, que un tipo válido
  se guarda y que los datos y los ids se conservan después de reiniciar la
  aplicación.

### Problemas que resolví

- El puerto 8080 estaba reservado por una regla de reenvío de VirtualBox.
- Las comillas del JSON en PowerShell; usé `Invoke-RestMethod`.
- La carpeta `data/` no estaba ignorada por Git.

## Uso declarado

Se usó IA (OpenCode con el modelo `opencode/big-pickle`) para este trabajo.
El código fue revisado y probado antes de commitearlo; la defensa en sala es
responsabilidad del alumno.

## Tercera sesión: Dominio JPA con herencia (Minecraft)

| Dato | Valor |
|---|---|
| **Marca / agente** | OpenCode (asistente de código en terminal) |
| **Modelo LLM exacto** | `opencode/big-pickle` |
| **Fecha** | 9 de noviembre de 2026 |

### Prompt usado

Instrucción dada al agente: "Adapta el dominio de Minecraft a JPA persistiendo tres clases (Entidad como padre, Zombie y Creeper como hijas), asegurando las anotaciones correspondientes de JPA, la herencia con estrategia JOINED y la interfaz de dominio requerida."

### Qué generó el agente

- Configuró `Entidad` como la entidad raíz utilizando `@Entity`, `@Inheritance(JOINED)` y `@DiscriminatorColumn("tipo")`.
- Ajustó a `Hostil` como `@MappedSuperclass`, dejando a `Zombie` y `Creeper` como las clases hijas persistidas.
- Creó la interfaz `EntidadRepository` extendiendo de `JpaRepository<Entidad, Long>`.
- Ejecutó la verificación mediante `./mvnw test`, logrando que pasaran **27 tests con 0 fallos** y confirmando que Hibernate genera correctamente las tablas en H2.

### Qué corregí o decidí yo

- Establecí y validé el flujo de trabajo híbrido: procesamiento de código y ejecución de pruebas en la máquina virtual Linux mediante SSH con OpenCode, reservando Windows y VS Code para la gestión de versiones y documentación.
- Verifiqué el estado de Git asegurando que el commit del Paso 1 quedara correctamente registrado y sincronizado.

### Problemas que resolví

- Bloqueo de directorios (`model` y `repository`) al realizar el `git pull` en Windows debido a archivos en caché o en uso por el entorno de desarrollo, resuelto purgando los directorios obsoletos y reintentando la sincronización de manera limpia.

## Tercera sesión: Implementación del CRUD REST (Entidad Hostil)

| Dato | Valor |
|---|---|
| **Marca / agente** | OpenCode (asistente de código en terminal) |
| **Modelo LLM exacto** | `opencode/DeepSeek v4.1 Flash 1017` |
| **Fecha** | 11 de noviembre de 2026 |

### Prompt usado

Instrucción dada al agente: Implementa el CRUD REST completo para la familia Hostil (que incluye las entidades Zombie y Creeper, heredando de Hostil según la estrategia JOINED ya configurada en el Paso 1). 

Por favor, asegúrate de incluir lo siguiente:
1. HostilService: Capa de negocio que contenga las validaciones necesarias para los atributos de los enemigos hostiles (como nombre, vida, velocidad y daño de ataque).
2. HostilController: Controlador REST que exponga los endpoints bajo la ruta "/api/hostiles" (GET para listar todos y por ID, POST para crear, PUT para actualizar y DELETE para eliminar).
3. DTOs: Clases DTO de petición y respuesta para desacoplar el modelo de persistencia de la API REST.
4. Repositorios necesarios si falta alguno para la jerarquía.

Verifica que todo compile correctamente manteniendo la persistencia con H2.

### Qué generó el agente

- Creó y estructuró los DTOs (`HostilDTO`) y la lógica de negocio en el servicio (`HostilService`), incorporando validaciones positivas para los atributos y el manejo de excepciones mediante el manejador correspondiente.
- Implementó los endpoints en el controlador bajo la ruta base `/api/hostiles` (abarcando operaciones `GET`, `POST`, `PUT`, `PATCH` y `DELETE`).
- Ejecutó la verificación mediante `./mvnw test`, logrando que pasaran **27 tests con 0 fallos**, manteniendo la compilación correcta y la persistencia en la base de datos H2.

### Qué corregí o decidí yo

- Configuré el reenvío de puertos (Port Forwarding) en VirtualBox para permitir la comunicación entre el cliente Insomnia en Windows y el servidor Spring Boot ejecutándose en la máquina virtual Linux.
- Diseñé y ejecuté la colección de pruebas en Insomnia (`insomnia_crud_hostil.json`), validando de forma interactiva los métodos del CRUD y exportando el resultado para la entrega.
- Sincronicé los flujos de trabajo locales y remotos para asegurar que tanto el código fuente como los archivos de prueba quedaran debidamente integrados en el repositorio de Git.

### Problemas que resolví

- Conflicto de rechazo en el `git push` (`[rejected] main -> main (fetch first)`) provocado por discrepancias de commits entre el entorno local y remoto, resuelto ejecutando un `git pull --rebase origin main` seguro en la máquina virtual antes de integrar la colección de Insomnia.