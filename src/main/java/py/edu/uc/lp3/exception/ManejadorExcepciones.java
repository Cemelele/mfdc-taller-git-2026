package py.edu.uc.lp3.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Un handler por cada hija de la jerarquia de excepciones del dominio.
 * El JSON incluye "tipo" para que el cliente distinga a las hermanas
 * aunque el codigo HTTP coincida.
 */
@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> noEncontrada(EntidadNoEncontradaException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(EntidadInvalidaException.class)
    public ResponseEntity<Map<String, Object>> entidadInvalida(EntidadInvalidaException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(SimulacionInvalidaException.class)
    public ResponseEntity<Map<String, Object>> simulacionInvalida(SimulacionInvalidaException ex) {
        return respuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado, EntidadException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("status", estado.value());
        cuerpo.put("error", estado.getReasonPhrase());
        cuerpo.put("tipo", ex.getClass().getSimpleName());
        cuerpo.put("mensaje", ex.getMessage());
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
