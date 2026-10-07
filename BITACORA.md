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

## Uso declarado

Se usó IA (OpenCode con el modelo `opencode/big-pickle`) para este trabajo.
El código fue revisado y probado antes de commitearlo; la defensa en sala es
responsabilidad del alumno.
