# Taller de Git y modelado orientado a objetos

**Asignatura:** Lenguaje de Programación 3 (CYT646)  
**Edición:** 2026  
**Classroom:** `nwtbf44v`

El repositorio personal **es** un proyecto **Spring Boot para API REST**, creado desde el starter de [start.spring.io](https://start.spring.io). Encima de ese esqueleto se practica Git y se engancha el modelado de Minecraft o Counter-Strike 2 (CS2).

**La entrega de POO no es un README.** Lo que se revisa es el dominio en Java (ocultamiento e herencia) dentro del Spring Boot, y el pull request (PR).

Pasar al chat del curso **el usuario de GitHub** antes de seguir.

---

## Parte A — Spring Boot (API REST) + repositorio

### A.1 Generar el proyecto en start.spring.io

Abrir [start.spring.io](https://start.spring.io) y usar **exactamente**:

| Campo | Valor |
|---|---|
| Project | **Maven** |
| Language | Java |
| Spring Boot | 3.x (estable) |
| Group | `py.edu.uc.lp3` |
| Artifact | `INICIALES-taller-git-2026` (ejemplo: `af-taller-git-2026`) |
| Name | el mismo artifact |
| Packaging | Jar |
| Java | **21** |
| Dependencies | **Spring Web** (API REST) |

*Generate* y descomprimir el zip. Ese directorio **es** el proyecto: trae `pom.xml`, wrapper Maven (`mvnw`), `.gitignore`, `README.md` y la clase `*Application`.

Comprobar **antes** de subirlo:

```bash
cd INICIALES-taller-git-2026
./mvnw spring-boot:run
```

Debe levantar en `http://localhost:8080`. Cortar con Ctrl+C. Si no arranca, no se sigue con Git.

No se borra la clase `*Application`. No se pega un tutorial entero de Spring. Hoy el valor está en el dominio y en cómo se versiona.

### A.2 `.gitignore`

El starter ya trae un `.gitignore`. Completarlo (o reemplazar por la unión) con la plantilla de [gitignore.io](https://www.toptal.com/developers/gitignore) para:

```text
Java,IntelliJ,Maven
```

Git versiona todo salvo lo listado ahí. No se sube `target/`, caches del IDE ni la máquina virtual.

### A.3 Crear el repo en GitHub

En [github.com/new](https://github.com/new):

| Campo | Valor |
|---|---|
| Nombre | `INICIALES-taller-git-2026` (igual al artifact) |
| Visibilidad | **Público** |
| README | **No** marcar *Add a README file* (el starter ya trae uno; si GitHub crea otro, hay conflicto al pushear) |
| `.gitignore` | **Ninguno** (lo trae el starter; se enriquece en A.2) |
| Licencia | **Apache License 2.0** |

**Apache License 2.0** es una licencia open source: quien acceda puede usar el contenido (incluso comercialmente), sin garantías. Hay que acreditar a quien corresponda. No obliga a publicar para siempre el código fuente.

Si GitHub solo deja agregar la licencia con un commit inicial, clonar, copiar encima los archivos del zip (sin pisar `LICENSE`) y resolver el `README` a mano.

### A.4 Primer commit: el esqueleto Spring Boot

En Linux o macOS: terminal. En Windows: **Git Bash**.

```bash
cd INICIALES-taller-git-2026
git init
git add -A
git status
git commit -m "chore: esqueleto Spring Boot API REST desde start.spring.io"
git branch -M main
git remote add origin git@github.com:MI_USER_DE_GITHUB/INICIALES-taller-git-2026.git
git push -u origin main
```

Reemplazar `MI_USER_DE_GITHUB` e `INICIALES`.

Si el repo en GitHub **ya** se clonó vacío (solo `LICENSE`):

```bash
git clone git@github.com:MI_USER_DE_GITHUB/INICIALES-taller-git-2026.git
# copiar adentro el contenido del zip (pom.xml, src/, mvnw, .gitignore, README.md)
git add -A
git commit -m "chore: esqueleto Spring Boot API REST desde start.spring.io"
git push
```

### A.5 Identidad Git (una vez por máquina)

```bash
git config --global user.name "mi-usuario-de-Github"
git config --global user.email "mi-email-asociado-a-cuenta-de-github@email.com"
```

Más configuraciones: [wiki Git (Joko)](https://joko.miraheze.org/wiki/Git#Configuraciones).

### A.6 SSH (si aún no está)

```bash
ssh-keygen -t ed25519 -C "your_email@example.com"
```

Copiar la clave pública (`~/.ssh/id_ed25519.pub`) a GitHub → *Settings* → *SSH and GPG keys*.

Si `origin` quedó en HTTPS:

```bash
git remote -v
git remote set-url origin git@github.com:MI_USER_DE_GITHUB/INICIALES-taller-git-2026.git
git push
```

---

## Parte B — Primeros commits sobre el Spring Boot

### B.1 Editar el README en la web y traer el cambio

El `README.md` del starter se **reemplaza en la web** por un texto corto: nombre, usuario GitHub, comisión CYT646 F, y cómo levantar la API:

```text
./mvnw spring-boot:run
```

Apuntar a `docs/TALLER_GIT.md` si copian esta guía al repo. **No** documentar ahí el diagrama de clases: eso va en el PR.

Commit en la web → en la máquina:

```bash
git pull
git log
```

`git log` es la bitácora: commits y hashes.

### B.2 `RUN.md` por la terminal

Crear `RUN.md` en la raíz (cómo arrancar la API, puerto 8080, JDK 21). Luego:

```bash
git status
git add RUN.md
# si hay varios archivos: git add -A
git status
git commit
git status
git push
```

Editar `RUN.md` otra vez y repetir `status` → `add` → `commit` → `push`.

### B.3 Viajar en el historial

```bash
git log          # copiar el hash del primer commit (el esqueleto Spring Boot)
git checkout HASH
```

HEAD detached: el directorio vuelve a aquella versión. Volver al estado actual:

```bash
git checkout main
```

(Si la rama por defecto es `master`, usar `master`.)

---

## Parte C — Colaboración: rama en el repo del compañero

El objetivo de Git no es “tener un repo”, es **integrar trabajo ajeno sin pisarse**. El repo del compañero **también** es un Spring Boot API REST.

### C.1 Clonar y ramificar

```bash
git clone git@github.com:USER_DEL_COMPAÑERO/INICIALES-taller-git-2026.git
cd INICIALES-taller-git-2026
git checkout -b INICIALES-contribucion-lp3
git status
```

Ejemplo de rama: `afq-contribucion-lp3`. Si no aparece:

```bash
git checkout INICIALES-contribucion-lp3
```

### C.2 Qué se agrega (modelado, no README)

**No se entrega “unas líneas más en el README” como ejercicio de POO.** El README del compañero se deja, salvo una línea en una tabla de contribuyentes si el dueño la pidió.

Lo que se agrega es **código en el Spring Boot** (Parte D). Si hace falta un rastro Git aparte:

```text
notas-INICIALES.txt    ← una línea con la fecha
```

### C.3 Push y PR

```bash
git add -p
git status
git commit -m "feat(minecraft): Creeper con invariante de vida encapsulado"
# o: feat(cs2): AWP con recarga encapsulada
git push origin INICIALES-contribucion-lp3
```

En GitHub: **Compare & pull request** hacia `main` del repo **del compañero**.

El *maintainer* revisa, compara y hace merge. Después del merge, `./mvnw spring-boot:run` tiene que seguir levantando.

### C.4 PR con pedido de cambio (obligatorio)

1. Otra rama en el repo del compañero (o en el propio, según cátedra).
2. Agregar `Changelog.md` (o `changelog-INICIALES.md` si ya existe).
3. Abrir un PR.
4. El reviewer **deja un comentario pidiendo un cambio concreto**.
5. Quien abrió el PR hace el cambio, commit y `git push` sobre **la misma rama**. El PR se actualiza solo.
6. Recién entonces se mergea.

---

## Parte D — Enganchar el modelado en el Spring Boot

Partir del diagrama y del código de POO-02 y POO-03. Elegir **una** opción. Las clases viven **dentro** del árbol que generó el starter.

### D.1 Dónde van las clases

El starter dejó algo como `src/main/java/py/edu/uc/lp3/inicialestallergit2026/`. **No se mueve** la clase `*Application`. Al lado, en el mismo `src/main/java`:

```text
src/main/java/py/edu/uc/lp3/
├── …/…Application.java                ← la del starter; no tocar la lógica
├── minecraft/                         opción A
│   ├── Entidad.java
│   ├── Mob.java
│   ├── Creeper.java                   especialización de UNA persona
│   └── web/
│       └── ApellidoNombreController.java
└── cs2/                               opción B
    ├── Arma.java
    ├── Rifle.java
    ├── Ak47.java
    └── web/
        └── ApellidoNombreController.java
```

Tests:

```text
src/test/java/py/edu/uc/lp3/minecraft/ApellidoNombreTest.java
src/test/java/py/edu/uc/lp3/cs2/ApellidoNombreTest.java
```

- Una especialización por persona. Si el archivo ya está, se elige otra entidad o arma.
- El controlador REST habla con **mensajes** del dominio (`recibirDaño`, `disparar`, listar), no lee ni escribe campos.
- No se describe el modelo en el `README.md`: va en la **descripción del PR**.

### D.2 Opción A — Minecraft

Hay entidades (jugador, mobs, animales, aldeanos). Comparten comportamiento y cada tipo actúa distinto.

El modelo debe permitir tratar cualquier entidad de forma uniforme (moverse, recibir daño, desaparecer) **sin un `if` por cada tipo concreto**.

Las reglas de vida, movimiento y muerte viven en la jerarquía. La clase padre concentra lo común; cada subclase solo redefine lo que cambia.

Nadie fuera de la jerarquía debe poder dejar una entidad en un estado imposible. Si un campo es `protected`, hay que justificar por qué un método no alcanza.

La API REST (un `@RestController` propio) expone esas operaciones sin romper el encapsulamiento.

Especializaciones sugeridas: Creeper, Zombie, Esqueleto, Enderman, Aldeano, Cerdo, Araña, Ghast, Blaze, Golem de hierro, Wither, Allay, Jugador.

### D.3 Opción B — Counter-Strike 2

Hay armas (pistolas, rifles, francotiradores, escopetas, subfusiles, granadas). Comparten un contrato; cada tipo dispara, recarga o explota distinto.

El inventario debe poder contener cualquier arma y pedirle que dispare, recargue o se ofrezca en la tienda **sin un `if` por cada tipo**.

El daño, la munición y el cooldown no se pisan desde afuera. Si dos armas copian el mismo método, falta generalización. Si una hija deja los campos `public`, falta ocultamiento.

La API REST expone el inventario y las acciones del arma por mensajes, no por campos.

Especializaciones sugeridas: AK-47, M4A4, AWP, Desert Eagle, USP-S, MP9, Nova, HE, Flashbang, Smoke, Molotov, cuchillo.

### D.4 Criterios de diseño

1. **Ocultamiento.** El estado cambia por mensajes, no por asignación directa desde el controlador ni desde otra clase.
2. **Reglas adentro de la clase.** Un setter que acepta cualquier número no oculta nada. Tras el constructor y tras cada mensaje (incluido el que llega por HTTP), el objeto sigue válido.
3. **Herencia de comportamiento.** Si sobreescribe, decide si llama a `super` o reemplaza; no ignore la regla del padre sin motivo.
4. **Visibilidad.** `private` por defecto. `protected` solo si la hija necesita un gancho real.
5. **REST no es el modelo.** Si para “hacer el endpoint” hubo que abrir los campos, el diseño no está listo.

Pregunta del reviewer: *¿puedo, desde el controlador o desde otra clase, dejar este objeto inválido?* Si sí, se pide cambio (mismo mecanismo que C.4).

### D.5 Descripción del PR

```markdown
## Dominio
Minecraft | CS2

## Qué se agrega
- Superclase usada: …
- Especialización: …
- Regla encapsulada: …
- Endpoint REST: método y ruta (sin pegar el tutorial)

## Cómo probar
./mvnw test
./mvnw spring-boot:run
# curl o Insomnia contra el endpoint propio

## Revisión pedida
¿Se puede romper el invariante desde fuera (incluido el controller)?
```

---

## Definition of Done

### Proyecto

- [ ] Usuario de GitHub pasado al chat
- [ ] Zip generado en start.spring.io (Maven, Java 21, **Spring Web**)
- [ ] `./mvnw spring-boot:run` funciona en local
- [ ] Repo público `INICIALES-taller-git-2026` con Apache License 2.0
- [ ] `.gitignore` cubre Java, IntelliJ y Maven (`target/` no está versionado)

### Git

- [ ] Clone/push por SSH; `user.name` y `user.email` configurados
- [ ] Primer commit = esqueleto Spring Boot
- [ ] README editado en la web + `git pull` + `git log`
- [ ] `RUN.md` commiteado y pusheado al menos dos veces
- [ ] `git checkout HASH` del primer commit y vuelta a `main`
- [ ] Rama `INICIALES-contribucion-lp3` en el repo del compañero
- [ ] PR mergeado (o abierto a espera de review); el Spring Boot del compañero sigue arrancando
- [ ] Segundo PR (`Changelog`) con comentario del reviewer, cambio aplicado y push a la misma rama

### POO + REST

- [ ] Código en `py.edu.uc.lp3.minecraft` **o** `py.edu.uc.lp3.cs2`
- [ ] Una especialización propia; comportamiento heredado o sobreescrito con criterio
- [ ] Invariante protegido (también frente al controller)
- [ ] Un `@RestController` propio que usa mensajes del dominio
- [ ] Test o prueba HTTP documentada en el PR
- [ ] Comentario de revisión en el PR de un compañero

---

**Cátedra:** ale.feltes@uc.edu.py · @alefeltes
