package py.edu.uc.lp3.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones del dominio a respuestas HTTP limpias.
 * Cada hija de HostilDomainException tiene su codigo: 404 para recursos
 * inexistentes y 400 para pedidos que rompen una regla de negocio.
 * El JSON incluye "tipo" para distinguir excepciones hermanas aunque
 * compartan el mismo codigo de estado.
 */
@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(CreeperNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> creeperNoEncontrado(CreeperNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex);
    }

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
        return respuesta(HttpStatus.BAD_REQUEST, ex);
    }

    /** Red de seguridad para cualquier excepcion nueva de la familia Hostil. */
    @ExceptionHandler(HostilDomainException.class)
    public ResponseEntity<Map<String, Object>> dominioInvalido(HostilDomainException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex);
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado,
                                                          HostilDomainException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("status", estado.value());
        cuerpo.put("error", estado.getReasonPhrase());
        cuerpo.put("tipo", ex.getClass().getSimpleName());
        cuerpo.put("mensaje", ex.getMessage());
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
