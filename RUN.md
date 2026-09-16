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
- Ejemplo:

```bash
curl -i localhost:8080
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