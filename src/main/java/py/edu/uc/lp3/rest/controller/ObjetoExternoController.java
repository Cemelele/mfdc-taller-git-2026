package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import py.edu.uc.lp3.rest.dto.ObjetoExternoDTO;
import py.edu.uc.lp3.service.ObjetoExternoService;

@RestController
@RequestMapping("/api/objetos")
@Tag(name = "Objetos externos", description = "Consulta de api.restful-api.dev con WebClient")
public class ObjetoExternoController {

    private final ObjetoExternoService service;

    public ObjetoExternoController(ObjetoExternoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar objetos externos",
            description = "Consulta GET https://api.restful-api.dev/objects mediante WebClient.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objetos obtenidos"),
            @ApiResponse(responseCode = "502", description = "Sin conexion con la API externa")
    })
    public List<ObjetoExternoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un objeto externo por id",
            description = "Consulta GET https://api.restful-api.dev/objects/{id} mediante WebClient.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Objeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Objeto no encontrado en la API externa"),
            @ApiResponse(responseCode = "502", description = "Sin conexion con la API externa")
    })
    public ObjetoExternoDTO buscar(
            @Parameter(description = "Identificador del objeto externo") @PathVariable String id) {
        return service.buscar(id);
    }
}
