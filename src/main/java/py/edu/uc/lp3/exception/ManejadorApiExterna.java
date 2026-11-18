package py.edu.uc.lp3.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class ManejadorApiExterna {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> respuestaExterna(WebClientResponseException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("status", ex.getStatusCode().value());
        cuerpo.put("error", ex.getStatusText());
        cuerpo.put("tipo", ex.getClass().getSimpleName());
        cuerpo.put("mensaje", "La API externa respondio con un error");
        return ResponseEntity.status(ex.getStatusCode()).body(cuerpo);
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<Map<String, Object>> sinConexion(WebClientRequestException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("status", HttpStatus.BAD_GATEWAY.value());
        cuerpo.put("error", HttpStatus.BAD_GATEWAY.getReasonPhrase());
        cuerpo.put("tipo", ex.getClass().getSimpleName());
        cuerpo.put("mensaje", "No se pudo conectar con la API externa");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(cuerpo);
    }
}
