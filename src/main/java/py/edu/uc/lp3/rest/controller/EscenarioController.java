package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.rest.dto.EscenarioDTO.CreeperElectrificadoRequest;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.EscenarioGuardado;
import py.edu.uc.lp3.service.EscenarioService;

@RestController
@RequestMapping("/api/escenarios/creeper-electrificado")
@Tag(name = "Escenarios", description = "Escenarios que combinan las simulaciones de A2 y A3")
public class EscenarioController {

    private final EscenarioService service;

    public EscenarioController(EscenarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear un escenario de Creeper electrificado",
            description = "Primero simula en A2 (Ruth) y luego en A3 (Sebastian). "
                    + "Solo guarda la jugada si ambos aceptan.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Escenario guardado"),
            @ApiResponse(responseCode = "400", description = "Datos del escenario invalidos"),
            @ApiResponse(responseCode = "409", description = "A2 o A3 rechazo la simulacion"),
            @ApiResponse(responseCode = "502", description = "A2 o A3 no esta disponible")
    })
    public ResponseEntity<EscenarioGuardado> crear(
            @RequestBody CreeperElectrificadoRequest peticion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(peticion));
    }

    @GetMapping
    @Operation(summary = "Listar escenarios guardados",
            description = "Devuelve las jugadas de Creeper electrificado persistidas en H2.")
    @ApiResponse(responseCode = "200", description = "Listado de escenarios")
    public List<EscenarioGuardado> listar() {
        return service.listar();
    }
}
