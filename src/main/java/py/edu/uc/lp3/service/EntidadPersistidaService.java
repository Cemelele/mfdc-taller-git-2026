package py.edu.uc.lp3.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.uc.lp3.model.EntidadPersistida;
import py.edu.uc.lp3.repository.EntidadPersistidaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EntidadPersistidaService {

    private static final Set<String> TIPOS_VALIDOS =
            Set.of("creeper", "zombie", "esqueleto", "enderman", "cerdo", "aldeano");

    private final EntidadPersistidaRepository repository;

    public EntidadPersistidaService(EntidadPersistidaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public EntidadPersistida crear(EntidadPersistida entidad) {
        validarEntidad(entidad);
        entidad.setTipo(entidad.getTipo().toLowerCase());
        return repository.save(entidad);
    }

    @Transactional(readOnly = true)
    public List<EntidadPersistida> listarTodas() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<EntidadPersistida> buscarPorId(Long id) {
        validarId(id);
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<EntidadPersistida> buscarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo no puede estar vacío");
        }
        return repository.findByTipo(tipo.toLowerCase());
    }

    @Transactional
    public EntidadPersistida actualizar(Long id, EntidadPersistida datos) {
        validarId(id);
        validarEntidad(datos);

        return repository.findById(id)
                .map(entidad -> {
                    entidad.setNombre(datos.getNombre());
                    entidad.setVida(datos.getVida());
                    entidad.setVelocidad(datos.getVelocidad());
                    entidad.setTipo(datos.getTipo().toLowerCase());
                    entidad.setDropItem(datos.getDropItem());
                    entidad.setImagen(datos.getImagen());
                    entidad.setSonido(datos.getSonido());
                    entidad.setPosX(datos.getPosX());
                    entidad.setPosY(datos.getPosY());
                    entidad.setPosZ(datos.getPosZ());
                    return repository.save(entidad);
                })
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la entidad con ID: " + id));
    }

    @Transactional
    public void eliminar(Long id) {
        validarId(id);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("No se encontró la entidad con ID: " + id);
        }
        repository.deleteById(id);
    }

    // ---- Validaciones (un único lugar para cada regla) ----

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
    }

    private void validarEntidad(EntidadPersistida e) {
        if (e == null) {
            throw new IllegalArgumentException("La entidad no puede ser nula");
        }
        if (e.getNombre() == null || e.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (e.getVida() <= 0) {
            throw new IllegalArgumentException("La vida debe ser mayor a 0");
        }
        if (e.getVelocidad() < 0) {
            throw new IllegalArgumentException("La velocidad no puede ser negativa");
        }
        // Regla de negocio: solo tipos que existen en el modelo de Minecraft
        if (e.getTipo() == null || !TIPOS_VALIDOS.contains(e.getTipo().toLowerCase())) {
            throw new IllegalArgumentException("Tipo inválido. Válidos: " + TIPOS_VALIDOS);
        }
    }
}