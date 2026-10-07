# Instrucciones de ejecución

API REST generada en [start.spring.io](https://start.spring.io) (Spring Boot 3.5.x, Java 21, Maven).

## Requisitos

- JDK 21
- Maven (o el wrapper `./mvnw` que ya viene incluido)

## Arrancar la API

```bash
./mvnw spring-boot:run
```

## Cómo probar

- La API levanta en el puerto **8080**: `http://localhost:8080`
- Endpoints:

```bash
curl -i localhost:8080                                     # GET / (IndexController)
curl -s localhost:8080/entidades                           # todas las entidades (JSON)
curl -s "localhost:8080/entidad/zombie?vida=35&danio=10&atacante=Creeper"
curl -i "localhost:8080/entidad/creeper?danio=-5"          # 400: la clase rechaza el daño negativo
```

- Para detenerla: `Ctrl+C`

## Verificación del entorno

```bash
java -version   # JDK 21
./mvnw -v       # Maven del wrapper
```

## Tests

```bash
./mvnw test
```