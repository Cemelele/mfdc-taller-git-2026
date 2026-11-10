package py.edu.uc.lp3.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<Map<String, Object>> noEncontrada(EntidadNoEncontradaException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(TipoInvalidoException.class)
    public ResponseEntity<Map<String, Object>> tipoInvalido(TipoInvalidoException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(SimulacionInvalidaException.class)
    public ResponseEntity<Map<String, Object>> simulacionInvalida(SimulacionInvalidaException ex) {
        return respuesta(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado, EntidadException ex) {
        return ResponseEntity.status(estado).body(Map.of(
                "status", estado.value(),
                "error", estado.getReasonPhrase(),
                "mensaje", ex.getMessage()));
    }
}