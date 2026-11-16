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
import py.edu.uc.lp3.exception.EstadoInvalidoException;
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
 * Simulacion persistida de explosiones. Aqui viven las reglas de dano por
 * distancia, electrificacion y explosion que antes estaban en Creeper.
 * Si una regla falla, @Transactional revierte todo.
 */
@Service
public class SimulacionService {

    private static final int DANIO_BASE = 15;
    private static final int DANIO_CARGADO = 25;

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
        validarCreeperPuedeExplotar(creeper);

        if (Boolean.TRUE.equals(peticion.cargado())) {
            cargarCreeper(creeper);
        }
        creeper.encender();

        SimulacionExplosion simulacion = new SimulacionExplosion(creeper.getId(),
                creeper.isCargado(), creeper.isEncendido(),
                creeper.getRadioExplosion(), danioBase(creeper));

        for (VictimaRequest victimaRequest : peticion.victimas()) {
            if (victimaRequest == null || victimaRequest.hostilId() == null) {
                throw new SimulacionInvalidaException("Cada victima necesita un hostilId");
            }
            double distancia = victimaRequest.distancia() == null ? 0.0 : victimaRequest.distancia();
            validarDistancia(distancia);
            if (victimaRequest.hostilId().equals(creeper.getId())) {
                throw new SimulacionInvalidaException("El Creeper no puede ser su propia victima");
            }
            Hostil victima = buscarHostil(victimaRequest.hostilId());

            int vidaAntes = victima.getVida();
            int danio = danioA(creeper, distancia);
            String detalle = danio > 0
                    ? victima.recibirDanio(danio, "Creeper")
                    : "Fuera del radio de la explosion: sin dano.";
            entidadRepository.save(victima);

            simulacion.agregarResultado(new ResultadoVictima(victima.getId(), tipoDe(victima),
                    distancia, danio, vidaAntes, victima.getVida(),
                    !victima.estaMuerto(), detalle));
        }

        // La explosion destruye al Creeper, cargado o no.
        creeper.morir();
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

    private void validarCreeperPuedeExplotar(Creeper creeper) {
        if (creeper.estaMuerto()) {
            throw new EstadoInvalidoException("El Creeper " + creeper.getId()
                    + " ya esta muerto (vida <= 0) o ya exploto: no puede explotar otra vez");
        }
    }

    private void validarDistancia(double distancia) {
        if (Double.isNaN(distancia) || Double.isInfinite(distancia) || distancia < 0) {
            throw new SimulacionInvalidaException("La distancia no puede ser negativa");
        }
    }

    private void cargarCreeper(Creeper creeper) {
        creeper.cargar();
    }

    private int danioBase(Creeper creeper) {
        return creeper.isCargado() ? DANIO_CARGADO : DANIO_BASE;
    }

    private int danioA(Creeper creeper, double distancia) {
        double radio = creeper.getRadioExplosion();
        if (radio <= 0 || distancia >= radio) {
            return 0;
        }
        return (int) Math.round(danioBase(creeper) * (1 - distancia / radio));
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
