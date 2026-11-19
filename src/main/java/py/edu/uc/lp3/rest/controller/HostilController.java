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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.rest.dto.HostilDTO.HostilRequest;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilResponse;
import py.edu.uc.lp3.service.HostilService;

/**
 * CRUD REST de la familia persistida Hostil (Zombie y Creeper).
 * El sustantivo de la URL es "hostiles"; el verbo HTTP indica la operacion.
 */
@RestController
@RequestMapping("/api/hostiles")
@Tag(name = "Hostiles", description = "CRUD de Zombie y Creeper persistidos")
public class HostilController {

    private final HostilService service;

    public HostilController(HostilService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear un hostil",
            description = "Crea un Zombie o un Creeper y lo persiste en H2.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Hostil creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos")
    })
    public ResponseEntity<HostilResponse> crear(@RequestBody HostilRequest peticion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(peticion));
    }

    @GetMapping
    @Operation(summary = "Listar hostiles",
            description = "Devuelve todos los hostiles o filtra por nombre.")
    @ApiResponse(responseCode = "200", description = "Listado de hostiles")
    public List<HostilResponse> listar(
            @Parameter(description = "Texto a buscar dentro del nombre") @RequestParam(required = false) String nombre) {
        return service.listar(nombre);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un hostil por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hostil encontrado"),
            @ApiResponse(responseCode = "404", description = "Hostil no encontrado")
    })
    public HostilResponse buscar(
            @Parameter(description = "Identificador del hostil") @PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Reemplazar un hostil",
            description = "PUT reemplaza el recurso completo; el tipo no se puede cambiar.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hostil actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Hostil no encontrado"),
            @ApiResponse(responseCode = "409", description = "El hostil esta muerto")
    })
    public HostilResponse actualizarTotal(@PathVariable Long id,
                                          @RequestBody HostilRequest peticion) {
        return service.actualizarTotal(id, peticion);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar parcialmente un hostil",
            description = "Modifica solo los campos enviados en el cuerpo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hostil actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Hostil no encontrado"),
            @ApiResponse(responseCode = "409", description = "El hostil esta muerto")
    })
    public HostilResponse actualizarParcial(@PathVariable Long id,
                                            @RequestBody HostilRequest peticion) {
        return service.actualizarParcial(id, peticion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un hostil")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Hostil eliminado"),
            @ApiResponse(responseCode = "404", description = "Hostil no encontrado")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del hostil") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint de lectura que usa el tipo de la interfaz Electrificable.
     * El controller no pregunta la clase concreta: el mensaje lo escribe
     * la implementacion (Creeper) y el estado cargado queda persistido.
     */
    @GetMapping("/{id}/rayo")
    @Operation(summary = "Electrificar un hostil",
            description = "Electrifica un Creeper persistido mediante la interfaz Electrificable.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rayo aplicado"),
            @ApiResponse(responseCode = "400", description = "El hostil no implementa Electrificable"),
            @ApiResponse(responseCode = "404", description = "Hostil no encontrado")
    })
    public Map<String, Object> rayo(
            @Parameter(description = "Identificador del hostil") @PathVariable Long id) {
        return service.recibirRayo(id);
    }
}
