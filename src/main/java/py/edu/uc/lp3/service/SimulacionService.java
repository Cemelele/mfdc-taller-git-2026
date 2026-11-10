package py.edu.uc.lp3.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import py.edu.uc.lp3.domain.Aldeano;
import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.domain.Zombie;
import py.edu.uc.lp3.exception.SimulacionInvalidaException;
import py.edu.uc.lp3.exception.TipoInvalidoException;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionResultado;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.Victima;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.VictimaResultado;

@Service
public class SimulacionService {

    public ExplosionResultado simularExplosion(ExplosionRequest peticion) {
        if (peticion == null || peticion.victimas() == null || peticion.victimas().isEmpty()) {
            throw new SimulacionInvalidaException("Debe indicar al menos una victima");
        }

        Creeper creeper = new Creeper();
        if (Boolean.TRUE.equals(peticion.cargado())) {
            creeper.recibirRayo();
        }

        List<VictimaResultado> resultados = new ArrayList<>();
        for (Victima victima : peticion.victimas()) {
            resultados.add(aplicarExplosion(creeper, victima));
        }

        return new ExplosionResultado(creeper.isCargado(), creeper.getRadioExplosion(),
                creeper.danioBase(), resultados);
    }

    private VictimaResultado aplicarExplosion(Creeper creeper, Victima victima) {
        if (victima == null || victima.distancia() == null) {
            throw new SimulacionInvalidaException("Cada victima necesita una distancia");
        }
        double distancia = victima.distancia();
        if (distancia < 0) {
            throw new SimulacionInvalidaException("La distancia no puede ser negativa");
        }

        Entidad entidad = crearVictima(victima.tipo());
        int danio = creeper.danioA(distancia);
        String detalle = danio > 0
                ? entidad.recibirDanio(danio, "Creeper")
                : "Fuera del radio de la explosion: sin dano.";

        return new VictimaResultado(victima.tipo().toLowerCase(), distancia, danio,
                entidad.getVida(), !entidad.estaMuerto(), detalle);
    }

    private Entidad crearVictima(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new TipoInvalidoException(tipo);
        }
        return switch (tipo.toLowerCase()) {
            case "zombie" -> new Zombie();
            case "aldeano" -> new Aldeano();
            default -> throw new TipoInvalidoException(tipo);
        };
    }
}