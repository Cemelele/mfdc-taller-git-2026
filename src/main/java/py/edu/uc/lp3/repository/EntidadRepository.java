package py.edu.uc.lp3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import py.edu.uc.lp3.domain.Entidad;

/**
 * Interfaz de dominio para persistir la jerarquia Entidad.
 *
 * Spring Data genera la implementacion: guarda y recupera tanto Zombie
 * como Creeper por el tipo padre Entidad, sin que el service conozca la
 * clase concreta. La columna discriminadora "tipo" devuelve la instancia
 * correcta al leer.
 */
@Repository
public interface EntidadRepository extends JpaRepository<Entidad, Long> {

    List<Entidad> findByNombreContainingIgnoreCase(String nombre);

    List<Entidad> findByVidaGreaterThan(int vida);
}
