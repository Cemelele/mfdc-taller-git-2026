package py.edu.uc.lp3.rest.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.rest.dto.HostilDTO.HostilRequest;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilResponse;
import py.edu.uc.lp3.service.HostilService;

/**
 * CRUD REST de la familia persistida Hostil (Zombie y Creeper).
 * El sustantivo de la URL es "hostiles"; el verbo HTTP indica la operacion.
 */
@RestController
@RequestMapping("/api/hostiles")
public class HostilController {

    private final HostilService service;

    public HostilController(HostilService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HostilResponse> crear(@RequestBody HostilRequest peticion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(peticion));
    }

    @GetMapping
    public List<HostilResponse> listar(@RequestParam(required = false) String nombre) {
        return service.listar(nombre);
    }

    @GetMapping("/{id}")
    public HostilResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    public HostilResponse actualizarTotal(@PathVariable Long id,
                                          @RequestBody HostilRequest peticion) {
        return service.actualizarTotal(id, peticion);
    }

    @PatchMapping("/{id}")
    public HostilResponse actualizarParcial(@PathVariable Long id,
                                            @RequestBody HostilRequest peticion) {
        return service.actualizarParcial(id, peticion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint de lectura que usa el tipo de la interfaz Electrificable.
     * El controller no pregunta la clase concreta: el mensaje lo escribe
     * la implementacion (Creeper) y el estado cargado queda persistido.
     */
    @GetMapping("/{id}/rayo")
    public Map<String, Object> rayo(@PathVariable Long id) {
        return service.recibirRayo(id);
    }
}
