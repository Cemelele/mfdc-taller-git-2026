package py.edu.uc.lp3.rest.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionResultado;
import py.edu.uc.lp3.service.SimulacionService;

@RestController
@RequestMapping("/api/simulaciones")
public class SimulacionController {

    private final SimulacionService service;

    public SimulacionController(SimulacionService service) {
        this.service = service;
    }

    @PostMapping("/explosion")
    public ExplosionResultado explosion(@RequestBody ExplosionRequest peticion) {
        return service.simularExplosion(peticion);
    }
}