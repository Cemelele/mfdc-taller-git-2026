package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Portero de la casa: GET / (http://localhost:8080/).
 *
 * No construye ninguna entidad. Solo confirma que el servicio esta vivo
 * y de que dominio se trata. Si esto no responde, el resto de la API
 * no se prueba.
 */
@RestController
@Tag(name = "Inicio", description = "Estado general del servicio")
public class IndexController {

    @GetMapping("/")
    @Operation(summary = "Estado del servicio",
            description = "Confirma que la aplicacion esta activa y resume los endpoints disponibles.")
    public String index() {
        return "Bienvenido al taller de Git 2026 - LP3. "
                + "Dominio: Minecraft. "
                + "CRUD: GET/POST /api/hostiles. "
                + "Simulacion: POST /api/simulaciones/explosion. "
                + "Explosion fija: POST /api/simulaciones/explosion-fija. "
                + "Interfaz: GET /api/hostiles/{id}/rayo o GET /rayo/cerdo.";
    }
}
