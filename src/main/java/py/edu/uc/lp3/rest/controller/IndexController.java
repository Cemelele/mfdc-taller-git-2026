package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Portero de la casa: GET / (http://localhost:8080/).
 *
 * No construye ninguna entidad. Solo confirma que el servicio esta vivo
 * y de que dominio se trata. Si esto no responde, el resto de la API
 * no se prueba.
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "Bienvenido al taller de Git 2026 - LP3. "
                + "Dominio: Minecraft. "
                + "Probá GET /entidad/zombie, GET /entidad/aldeano?profesion=Herrero o GET /entidades";
    }
}
