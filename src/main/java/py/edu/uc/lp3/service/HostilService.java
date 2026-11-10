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
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilRequest;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilResponse;

/**
 * Capa de negocio del CRUD. Valida el cuerpo, construye la hija concreta
 * (Zombie o Creeper), aplica los mensajes del dominio y persiste.
 */
@Service
public class HostilService {

    private static final int VIDA_POR_DEFECTO = 20;

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
        if (peticion == null) {
            throw new EntidadInvalidaException("El cuerpo de la actualizacion no puede estar vacio");
        }
        validarTipoInmutable(hostil, peticion.tipo());
        try {
            if (peticion.nombre() != null) {
                hostil.renombrar(peticion.nombre());
            }
            if (peticion.vida() != null) {
                hostil.ajustarVida(peticion.vida());
            }
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
                creeper.recibirRayo();
            }
            if (Boolean.TRUE.equals(peticion.encendido()) && hostil instanceof Creeper creeper) {
                creeper.encender();
            }
        } catch (IllegalArgumentException e) {
            throw new EntidadInvalidaException(e.getMessage());
        }
        return HostilResponse.desde(repository.save(hostil));
    }

    @Transactional
    public HostilResponse actualizarTotal(Long id, HostilRequest peticion) {
        Hostil hostil = buscarEntidad(id);
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
        validarTipoInmutable(hostil, peticion.tipo());
        try {
            hostil.renombrar(peticion.nombre());
            hostil.ajustarVida(peticion.vida());
            hostil.cambiarVelocidad(peticion.velocidad());
            if (peticion.rangoAggro() != null) {
                hostil.cambiarRangoAggro(peticion.rangoAggro());
            }
            if (peticion.danioAtaque() != null) {
                hostil.cambiarDanioAtaque(peticion.danioAtaque());
            }
            if (Boolean.TRUE.equals(peticion.cargado()) && hostil instanceof Creeper creeper) {
                creeper.recibirRayo();
            }
            if (Boolean.TRUE.equals(peticion.encendido()) && hostil instanceof Creeper creeper) {
                creeper.encender();
            }
        } catch (IllegalArgumentException e) {
            throw new EntidadInvalidaException(e.getMessage());
        }
        return HostilResponse.desde(repository.save(hostil));
    }

    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscarEntidad(id));
    }

    /**
     * Endpoint que habla por el tipo de la interfaz Electrificable, no por
     * la clase concreta. Solo el Creeper de la familia persistida la implementa.
     */
    @Transactional
    public Map<String, Object> recibirRayo(Long id) {
        Hostil hostil = buscarEntidad(id);
        if (!(hostil instanceof Electrificable electrificable)) {
            throw new EntidadInvalidaException(
                    tipoDe(hostil) + " no implementa Electrificable en esta familia persistida");
        }
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("hostilId", hostil.getId());
        json.put("tipo", tipoDe(hostil));
        json.put("avisoRayo", electrificable.avisoRayo());
        json.put("recibirRayo", electrificable.recibirRayo());
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
            if (nombre.isBlank()) {
                throw new EntidadInvalidaException("El nombre no puede estar vacio");
            }
            nombre = nombre.trim();
        }
        int vida = peticion.vida() == null ? VIDA_POR_DEFECTO : peticion.vida();
        if (vida <= 0) {
            throw new EntidadInvalidaException("La vida debe ser mayor a 0");
        }
        if (peticion.velocidad() != null && peticion.velocidad() < 0) {
            throw new EntidadInvalidaException("La velocidad no puede ser negativa");
        }
        if (peticion.rangoAggro() != null && peticion.rangoAggro() <= 0) {
            throw new EntidadInvalidaException("El rango de aggro debe ser positivo");
        }
        if (peticion.danioAtaque() != null && peticion.danioAtaque() <= 0) {
            throw new EntidadInvalidaException("El dano de ataque debe ser positivo");
        }
        try {
            Hostil hostil = switch (tipo) {
                case "zombie" -> nombre == null
                        ? new Zombie(vida)
                        : new Zombie(nombre, vida);
                case "creeper" -> new Creeper();
                default -> throw new EntidadInvalidaException(
                        "Tipo invalido: " + peticion.tipo() + ". Validos: zombie, creeper");
            };
            if (tipo.equals("creeper")) {
                if (nombre != null) {
                    hostil.renombrar(nombre);
                }
                hostil.ajustarVida(vida);
            }
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
                creeper.recibirRayo();
            }
            if (Boolean.TRUE.equals(peticion.encendido()) && hostil instanceof Creeper creeper) {
                creeper.encender();
            }
            return hostil;
        } catch (IllegalArgumentException e) {
            throw new EntidadInvalidaException(e.getMessage());
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
