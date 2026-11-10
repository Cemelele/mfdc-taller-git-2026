package py.edu.uc.lp3.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.domain.Hostil;
import py.edu.uc.lp3.exception.CreeperNoEncontradoException;
import py.edu.uc.lp3.exception.EntidadInvalidaException;
import py.edu.uc.lp3.exception.EntidadNoEncontradaException;
import py.edu.uc.lp3.exception.SimulacionInvalidaException;
import py.edu.uc.lp3.model.ResultadoVictima;
import py.edu.uc.lp3.model.SimulacionExplosion;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.repository.SimulacionExplosionRepository;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionResultado;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.VictimaRequest;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.VictimaResultado;

/**
 * Simulacion persistida de explosiones. Aplica las dos reglas del dominio
 * sobre Hostil/Zombie/Creeper, guarda el resultado y deja el estado de todos
 * los participantes en H2. Si una regla falla, @Transactional revierte todo.
 */
@Service
public class SimulacionService {

    private final EntidadRepository entidadRepository;
    private final SimulacionExplosionRepository simulacionRepository;

    public SimulacionService(EntidadRepository entidadRepository,
                             SimulacionExplosionRepository simulacionRepository) {
        this.entidadRepository = entidadRepository;
        this.simulacionRepository = simulacionRepository;
    }

    @Transactional
    public ExplosionResultado simularExplosion(ExplosionRequest peticion) {
        if (peticion == null) {
            throw new SimulacionInvalidaException("La peticion de simulacion no puede estar vacia");
        }
        if (peticion.creeperId() == null) {
            throw new SimulacionInvalidaException("Debe indicar el creeperId que explota");
        }
        if (peticion.victimas() == null || peticion.victimas().isEmpty()) {
            throw new SimulacionInvalidaException("Debe indicar al menos una victima");
        }

        Creeper creeper = buscarCreeper(peticion.creeperId());

        if (Boolean.TRUE.equals(peticion.cargado())) {
            creeper.recibirRayo();
        }
        creeper.encender();

        SimulacionExplosion simulacion = new SimulacionExplosion(creeper.getId(),
                creeper.isCargado(), creeper.isEncendido(),
                creeper.getRadioExplosion(), creeper.danioBase());

        for (VictimaRequest victimaRequest : peticion.victimas()) {
            if (victimaRequest == null || victimaRequest.hostilId() == null) {
                throw new SimulacionInvalidaException("Cada victima necesita un hostilId");
            }
            double distancia = victimaRequest.distancia() == null ? 0.0 : victimaRequest.distancia();
            if (distancia < 0) {
                throw new SimulacionInvalidaException("La distancia no puede ser negativa");
            }
            if (victimaRequest.hostilId().equals(creeper.getId())) {
                throw new SimulacionInvalidaException("El Creeper no puede ser su propia victima");
            }
            Hostil victima = buscarHostil(victimaRequest.hostilId());

            int vidaAntes = victima.getVida();
            int danio = creeper.danioA(distancia);
            String detalle = danio > 0
                    ? victima.recibirDanio(danio, "Creeper")
                    : "Fuera del radio de la explosion: sin dano.";
            entidadRepository.save(victima);

            simulacion.agregarResultado(new ResultadoVictima(victima.getId(), tipoDe(victima),
                    distancia, danio, vidaAntes, victima.getVida(),
                    !victima.estaMuerto(), detalle));
        }

        // Regla 2: la explosion destruye al Creeper, cargado o no.
        creeper.explotar();
        entidadRepository.save(creeper);
        simulacion.marcarCreeperDestruido();

        return mapear(simulacionRepository.save(simulacion));
    }

    @Transactional(readOnly = true)
    public List<SimulacionExplosion> listar() {
        return simulacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SimulacionExplosion buscar(Long id) {
        if (id == null) {
            throw new EntidadNoEncontradaException((Long) null);
        }
        return simulacionRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(id));
    }

    private ExplosionResultado mapear(SimulacionExplosion simulacion) {
        List<VictimaResultado> victimas = simulacion.getVictimas().stream()
                .map(r -> new VictimaResultado(r.getHostilId(), r.getTipo(), r.getDistancia(),
                        r.getDanio(), r.getVidaAntes(), r.getVidaDespues(), r.isViva(), r.getDetalle()))
                .toList();
        return new ExplosionResultado(simulacion.getId(), simulacion.getCreeperId(),
                simulacion.isCreeperCargado(), simulacion.isCreeperEncendido(),
                simulacion.getRadio(), simulacion.getDanioBase(),
                simulacion.isCreeperDestruido(), victimas);
    }

    private Creeper buscarCreeper(Long id) {
        if (id == null) {
            throw new CreeperNoEncontradoException((Long) null);
        }
        Entidad entidad = entidadRepository.findById(id)
                .orElseThrow(() -> new CreeperNoEncontradoException(id));
        if (!(entidad instanceof Creeper creeper)) {
            throw new EntidadInvalidaException("El hostil " + id
                    + " no es un Creeper: no puede explotar");
        }
        return creeper;
    }

    private Hostil buscarHostil(Long id) {
        if (id == null) {
            throw new EntidadNoEncontradaException((Long) null);
        }
        return entidadRepository.findById(id)
                .filter(Hostil.class::isInstance)
                .map(Hostil.class::cast)
                .orElseThrow(() -> new EntidadNoEncontradaException(id));
    }

    private String tipoDe(Hostil hostil) {
        return hostil.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }
}
