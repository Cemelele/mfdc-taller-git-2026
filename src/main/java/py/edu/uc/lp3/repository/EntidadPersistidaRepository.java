package py.edu.uc.lp3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.model.EntidadPersistida;

import java.util.List;

@Repository
public interface EntidadPersistidaRepository extends JpaRepository<EntidadPersistida, Long> {

    List<EntidadPersistida> findByTipo(String tipo);

    List<EntidadPersistida> findByNombreContainingIgnoreCase(String nombre);
}
