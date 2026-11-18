package py.edu.uc.lp3.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.uc.lp3.rest.dto.ObjetoExternoDTO;
import py.edu.uc.lp3.service.ObjetoExternoService;

@RestController
@RequestMapping("/api/objetos")
public class ObjetoExternoController {

    private final ObjetoExternoService service;

    public ObjetoExternoController(ObjetoExternoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ObjetoExternoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ObjetoExternoDTO buscar(@PathVariable String id) {
        return service.buscar(id);
    }
}
