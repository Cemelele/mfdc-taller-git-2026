package py.edu.uc.lp3.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Electrificable;
import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.domain.Hostil;
import py.edu.uc.lp3.domain.Zombie;
import py.edu.uc.lp3.exception.EntidadInvalidaException;
import py.edu.uc.lp3.exception.EntidadNoEncontradaException;
import py.edu.uc.lp3.exception.EstadoInvalidoException;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilRequest;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilResponse;

/**
 * Capa de negocio del CRUD. Concentra todas las validaciones de datos y
 * las reglas de ciclo de vida: el dominio solo muta su estado.
 */
@Service
public class HostilService {

    private static final int VIDA_POR_DEFECTO = 20;
    private static final String AVISO_RAYO_CREEPER =
            "El Creeper sisea mas fuerte mientras el rayo lo carga.";
    private static final String RAYO_CREEPER =
            "Creeper cargado por un rayo: ahora explota mas fuerte.";

    private final EntidadRepository repository;

    public HostilService(EntidadRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public HostilResponse crear(HostilRequest peticion) {
        Hostil hostil = construir(peticion);
        return HostilResponse.desde(repository.save(hostil));
    }

    @Transactional(readOnly = true)
    public List<HostilResponse> listar(String nombre) {
        List<Entidad> entidades = (nombre == null || nombre.isBlank())
                ? repository.findAll()
                : repository.findByNombreContainingIgnoreCase(nombre);
        return entidades.stream()
                .filter(Hostil.class::isInstance)
                .map(Hostil.class::cast)
                .map(HostilResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public HostilResponse buscar(Long id) {
        return HostilResponse.desde(buscarEntidad(id));
    }

    @Transactional
    public HostilResponse actualizarParcial(Long id, HostilRequest peticion) {
        Hostil hostil = buscarEntidad(id);
        validarEstadoVivo(hostil);
        if (peticion == null) {
            throw new EntidadInvalidaException("El cuerpo de la actualizacion no puede estar vacio");
        }
        validarTipoInmutable(hostil, peticion.tipo());
        if (peticion.nombre() != null) {
            validarNombre(peticion.nombre(), "El nombre no puede estar vacio");
            hostil.renombrar(peticion.nombre().trim());
        }
        if (peticion.vida() != null) {
            validarVida(peticion.vida(), "La vida debe ser mayor a 0");
            hostil.ajustarVida(peticion.vida());
        }
        if (peticion.velocidad() != null) {
            validarVelocidad(peticion.velocidad());
            hostil.cambiarVelocidad(peticion.velocidad());
        }
        if (peticion.rangoAggro() != null) {
            validarRangoAggro(peticion.rangoAggro());
            hostil.cambiarRangoAggro(peticion.rangoAggro());
        }
        if (peticion.danioAtaque() != null) {
            validarDanioAtaque(peticion.danioAtaque());
            hostil.cambiarDanioAtaque(peticion.danioAtaque());
        }
        if (Boolean.TRUE.equals(peticion.cargado()) && hostil instanceof Creeper creeper) {
            cargarCreeper(creeper);
        }
        if (Boolean.TRUE.equals(peticion.encendido()) && hostil instanceof Creeper creeper) {
            creeper.encender();
        }
        return HostilResponse.desde(repository.save(hostil));
    }

    @Transactional
    public HostilResponse actualizarTotal(Long id, HostilRequest peticion) {
        Hostil hostil = buscarEntidad(id);
        validarEstadoVivo(hostil);
        if (peticion == null) {
            throw new EntidadInvalidaException("El cuerpo de la actualizacion no puede estar vacio");
        }
        if (peticion.nombre() == null || peticion.nombre().isBlank()) {
            throw new EntidadInvalidaException("PUT reemplaza el recurso: falta el nombre");
        }
        if (peticion.vida() == null) {
            throw new EntidadInvalidaException("PUT reemplaza el recurso: falta la vida");
        }
        if (peticion.velocidad() == null) {
            throw new EntidadInvalidaException("PUT reemplaza el recurso: falta la velocidad");
        }
        validarNombre(peticion.nombre(), "El nombre no puede estar vacio");
        validarVida(peticion.vida(), "La vida debe ser mayor a 0");
        validarVelocidad(peticion.velocidad());
        if (peticion.rangoAggro() != null) {
            validarRangoAggro(peticion.rangoAggro());
        }
        if (peticion.danioAtaque() != null) {
            validarDanioAtaque(peticion.danioAtaque());
        }
        validarTipoInmutable(hostil, peticion.tipo());

        hostil.renombrar(peticion.nombre().trim());
        hostil.ajustarVida(peticion.vida());
        hostil.cambiarVelocidad(peticion.velocidad());
        if (peticion.rangoAggro() != null) {
            hostil.cambiarRangoAggro(peticion.rangoAggro());
        }
        if (peticion.danioAtaque() != null) {
            hostil.cambiarDanioAtaque(peticion.danioAtaque());
        }
        if (Boolean.TRUE.equals(peticion.cargado()) && hostil instanceof Creeper creeper) {
            cargarCreeper(creeper);
        }
        if (Boolean.TRUE.equals(peticion.encendido()) && hostil instanceof Creeper creeper) {
            creeper.encender();
        }
        return HostilResponse.desde(repository.save(hostil));
    }

    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscarEntidad(id));
    }

    /**
     * Endpoint que habla por el tipo de la interfaz Electrificable, no por
     * la clase concreta. La electrificacion del Creeper se resuelve aqui,
     * en el service, y no en la capa de dominio.
     */
    @Transactional
    public Map<String, Object> recibirRayo(Long id) {
        Hostil hostil = buscarEntidad(id);
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("hostilId", hostil.getId());
        json.put("tipo", tipoDe(hostil));
        if (hostil instanceof Creeper creeper) {
            json.put("avisoRayo", AVISO_RAYO_CREEPER);
            json.put("recibirRayo", cargarCreeper(creeper));
        } else if (hostil instanceof Electrificable electrificable) {
            json.put("avisoRayo", electrificable.avisoRayo());
            json.put("recibirRayo", electrificable.recibirRayo());
        } else {
            throw new EntidadInvalidaException(
                    tipoDe(hostil) + " no implementa Electrificable en esta familia persistida");
        }
        repository.save(hostil);
        return json;
    }

    private Hostil construir(HostilRequest peticion) {
        if (peticion == null) {
            throw new EntidadInvalidaException("El cuerpo de la entidad no puede estar vacio");
        }
        String tipo = normalizarTipo(peticion.tipo());
        String nombre = peticion.nombre();
        if (nombre != null) {
            validarNombre(nombre, "El nombre no puede estar vacio");
            nombre = nombre.trim();
        }
        int vida = peticion.vida() == null ? VIDA_POR_DEFECTO : peticion.vida();
        validarVida(vida, "La vida debe ser mayor a 0");
        if (peticion.velocidad() != null) {
            validarVelocidad(peticion.velocidad());
        }
        if (peticion.rangoAggro() != null) {
            validarRangoAggro(peticion.rangoAggro());
        }
        if (peticion.danioAtaque() != null) {
            validarDanioAtaque(peticion.danioAtaque());
        }

        Hostil hostil = switch (tipo) {
            case "zombie" -> nombre == null
                    ? new Zombie(vida)
                    : new Zombie(nombre, vida);
            case "creeper" -> new Creeper(nombre == null ? "Creeper" : nombre, vida);
            default -> throw new EntidadInvalidaException(
                    "Tipo invalido: " + peticion.tipo() + ". Validos: zombie, creeper");
        };
        if (peticion.velocidad() != null) {
            hostil.cambiarVelocidad(peticion.velocidad());
        }
        if (peticion.rangoAggro() != null) {
            hostil.cambiarRangoAggro(peticion.rangoAggro());
        }
        if (peticion.danioAtaque() != null) {
            hostil.cambiarDanioAtaque(peticion.danioAtaque());
        }
        if (Boolean.TRUE.equals(peticion.cargado()) && hostil instanceof Creeper creeper) {
            cargarCreeper(creeper);
        }
        if (Boolean.TRUE.equals(peticion.encendido()) && hostil instanceof Creeper creeper) {
            creeper.encender();
        }
        return hostil;
    }

    private String cargarCreeper(Creeper creeper) {
        creeper.cargar();
        return RAYO_CREEPER;
    }

    private void validarEstadoVivo(Hostil hostil) {
        if (hostil.estaMuerto()) {
            throw new EstadoInvalidoException("La entidad " + hostil.getId()
                    + " esta muerta (vida <= 0) y no puede revivir ni modificarse");
        }
    }

    private void validarNombre(String nombre, String mensaje) {
        if (nombre == null || nombre.isBlank()) {
            throw new EntidadInvalidaException(mensaje);
        }
    }

    private void validarVida(int vida, String mensaje) {
        if (vida <= 0) {
            throw new EntidadInvalidaException(mensaje);
        }
    }

    private void validarVelocidad(double velocidad) {
        if (velocidad < 0) {
            throw new EntidadInvalidaException("La velocidad no puede ser negativa");
        }
    }

    private void validarRangoAggro(double rangoAggro) {
        if (rangoAggro <= 0) {
            throw new EntidadInvalidaException("El rango de aggro debe ser positivo");
        }
    }

    private void validarDanioAtaque(int danioAtaque) {
        if (danioAtaque <= 0) {
            throw new EntidadInvalidaException("El dano de ataque debe ser positivo");
        }
    }

    private void validarTipoInmutable(Hostil hostil, String tipoPedido) {
        if (tipoPedido == null || tipoPedido.isBlank()) {
            return;
        }
        String tipoActual = tipoDe(hostil);
        if (!tipoActual.equals(tipoPedido.toLowerCase(Locale.ROOT))) {
            throw new EntidadInvalidaException("El tipo de un recurso persistido no se puede cambiar: "
                    + tipoActual + " != " + tipoPedido);
        }
    }

    private String normalizarTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new EntidadInvalidaException("El tipo es obligatorio: zombie o creeper");
        }
        return tipo.toLowerCase(Locale.ROOT);
    }

    private String tipoDe(Hostil hostil) {
        return hostil.getClass().getSimpleName().toLowerCase(Locale.ROOT);
    }

    private Hostil buscarEntidad(Long id) {
        if (id == null) {
            throw new EntidadNoEncontradaException((Long) null);
        }
        return repository.findById(id)
                .filter(Hostil.class::isInstance)
                .map(Hostil.class::cast)
                .orElseThrow(() -> new EntidadNoEncontradaException(id));
    }
}
