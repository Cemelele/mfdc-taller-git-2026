package py.edu.uc.lp3.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Entidad;
import py.edu.uc.lp3.domain.Zombie;

/**
 * Verifica que la jerarquia JPA guarde Zombie y Creeper por el tipo padre
 * Entidad y que al leer se reconstruya la clase concreta con la columna
 * discriminadora.
 */
@DataJpaTest
class EntidadRepositoryTest {

    @Autowired
    private EntidadRepository repository;

    @Test
    void guardaYRecuperaZombieYCreeperPorElTipoPadre() {
        Zombie zombie = repository.save(new Zombie("Zombie", 30));
        Creeper creeper = new Creeper();
        creeper.recibirRayo();
        creeper = repository.save(creeper);

        List<Entidad> entidades = repository.findAll();
        assertEquals(2, entidades.size());
        assertTrue(entidades.stream().anyMatch(Zombie.class::isInstance));
        assertTrue(entidades.stream().anyMatch(Creeper.class::isInstance));

        Entidad recuperada = repository.findById(creeper.getId()).orElseThrow();
        assertInstanceOf(Creeper.class, recuperada);
        assertTrue(((Creeper) recuperada).isCargado());
    }

    @Test
    void consultaLosCamposHeredadosDesdeLaEntidadRaiz() {
        repository.save(new Zombie("Zombie", 30));
        repository.save(new Zombie("Zombie debil", 5));

        assertEquals(1, repository.findByVidaGreaterThan(10).size());
        assertEquals(1, repository.findByNombreContainingIgnoreCase("debil").size());
    }
}
