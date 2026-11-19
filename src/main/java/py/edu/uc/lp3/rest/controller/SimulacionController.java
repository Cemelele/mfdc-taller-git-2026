package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.model.SimulacionExplosion;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionResultado;
import py.edu.uc.lp3.service.SimulacionService;

@RestController
@RequestMapping("/api/simulaciones")
@Tag(name = "Simulaciones", description = "Simulacion y persistencia de explosiones de Creeper")
public class SimulacionController {

    private final SimulacionService service;

    public SimulacionController(SimulacionService service) {
        this.service = service;
    }

    @PostMapping("/explosion")
    @Operation(summary = "Simular una explosion",
            description = "Calcula el danio por distancia, destruye al Creeper y persiste la simulacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Simulacion creada"),
            @ApiResponse(responseCode = "400", description = "Datos de la simulacion invalidos"),
            @ApiResponse(responseCode = "409", description = "El Creeper ya exploto o esta muerto")
    })
    public ResponseEntity<ExplosionResultado> explosion(@RequestBody ExplosionRequest peticion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.simularExplosion(peticion));
    }

    @GetMapping
    @Operation(summary = "Listar simulaciones",
            description = "Devuelve todas las simulaciones de explosion persistidas.")
    @ApiResponse(responseCode = "200", description = "Listado de simulaciones")
    public List<SimulacionExplosion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar una simulacion por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Simulacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Simulacion no encontrada")
    })
    public SimulacionExplosion buscar(
            @Parameter(description = "Identificador de la simulacion") @PathVariable Long id) {
        return service.buscar(id);
    }
}
