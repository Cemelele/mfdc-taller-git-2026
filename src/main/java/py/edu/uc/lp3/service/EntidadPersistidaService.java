package py.edu.uc.lp3.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.model.EntidadPersistida;
import py.edu.uc.lp3.repository.EntidadPersistidaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EntidadPersistidaService {

    private final EntidadPersistidaRepository repository;

    public EntidadPersistidaService(EntidadPersistidaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public EntidadPersistida crear(EntidadPersistida entidad) {
        validarEntidad(entidad);
        if (entidad.getVida() <= 0) {
            throw new IllegalArgumentException("La vida de la entidad debe ser positiva");
        }
        if (entidad.getVelocidad() < 0) {
            throw new IllegalArgumentException("La velocidad no puede ser negativa");
        }
        if (entidad.getNombre() == null || entidad.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la entidad no puede estar vacío");
        }
        return repository.save(entidad);
    }

    @Transactional(readOnly = true)
    public List<EntidadPersistida> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EntidadPersistida> buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<EntidadPersistida> buscarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo no puede estar vacío");
        }
        return repository.findByTipo(tipo);
    }

    @Transactional
    public EntidadPersistida actualizar(Long id, EntidadPersistida entidadActualizada) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
        validarEntidad(entidadActualizada);
        
        return repository.findById(id)
                .map(entidad -> {
                    if (entidadActualizada.getVida() <= 0) {
                        throw new IllegalArgumentException("La vida debe ser positiva");
                    }
                    if (entidadActualizada.getVelocidad() < 0) {
                        throw new IllegalArgumentException("La velocidad no puede ser negativa");
                    }
                    entidad.setNombre(entidadActualizada.getNombre());
                    entidad.setVida(entidadActualizada.getVida());
                    entidad.setVelocidad(entidadActualizada.getVelocidad());
                    entidad.setTipo(entidadActualizada.getTipo());
                    entidad.setDropItem(entidadActualizada.getDropItem());
                    entidad.setImagen(entidadActualizada.getImagen());
                    entidad.setSonido(entidadActualizada.getSonido());
                    entidad.setPosX(entidadActualizada.getPosX());
                    entidad.setPosY(entidadActualizada.getPosY());
                    entidad.setPosZ(entidadActualizada.getPosZ());
                    return repository.save(entidad);
                })
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la entidad con ID: " + id));
    }

    @Transactional
    public void eliminar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la entidad con ID: " + id);
        }
        repository.deleteById(id);
    }

    private void validarEntidad(EntidadPersistida entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (entidad.getNombre() == null || entidad.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (entidad.getVida() <= 0) {
            throw new IllegalArgumentException("La vida debe ser mayor a 0");
        }
        if (entidad.getVelocidad() < 0) {
            throw new IllegalArgumentException("La velocidad no puede ser negativa");
        }
    }
}
