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

import py.edu.uc.lp3.model.SimulacionExplosion;
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
    public ResponseEntity<ExplosionResultado> explosion(@RequestBody ExplosionRequest peticion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.simularExplosion(peticion));
    }

    @GetMapping
    public List<SimulacionExplosion> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public SimulacionExplosion buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}
