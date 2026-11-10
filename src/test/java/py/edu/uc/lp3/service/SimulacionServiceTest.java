package py.edu.uc.lp3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.domain.Zombie;
import py.edu.uc.lp3.model.SimulacionExplosion;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.repository.SimulacionExplosionRepository;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionRequest;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.ExplosionResultado;
import py.edu.uc.lp3.rest.dto.SimulacionDTO.VictimaRequest;

@DataJpaTest
@Import(SimulacionService.class)
class SimulacionServiceTest {

    @Autowired
    private SimulacionService service;

    @Autowired
    private EntidadRepository entidadRepository;

    @Autowired
    private SimulacionExplosionRepository simulacionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void laExplosionDestruyeAlCreeperYGuardaElDanoDeLasVictimas() {
        Long creeperId = entidadRepository.save(new Creeper()).getId();
        Long zombieId = entidadRepository.save(new Zombie("Zombie", 30)).getId();

        ExplosionResultado resultado = service.simularExplosion(new ExplosionRequest(
                creeperId, false,
                List.of(new VictimaRequest(zombieId, 1.0))));

        entityManager.flush();
        entityManager.clear();

        Creeper creeperPersistido = (Creeper) entidadRepository.findById(creeperId).orElseThrow();
        assertTrue(creeperPersistido.estaMuerto());
        assertEquals(0, creeperPersistido.getVida());
        assertTrue(creeperPersistido.isEncendido());

        Zombie zombiePersistido = (Zombie) entidadRepository.findById(zombieId).orElseThrow();
        assertTrue(zombiePersistido.getVida() < 30, "la victima debe recibir dano");

        SimulacionExplosion simulacion = simulacionRepository
                .findById(resultado.simulacionId()).orElseThrow();
        assertTrue(simulacion.isCreeperDestruido());
        assertTrue(simulacion.isCreeperEncendido());
        assertEquals(creeperId, simulacion.getCreeperId());
        assertEquals(1, simulacion.getVictimas().size());
        assertEquals(zombieId, simulacion.getVictimas().get(0).getHostilId());
    }

    @Test
    void unCreeperCargadoGuardaElDanoBaseMayor() {
        Long creeperId = entidadRepository.save(new Creeper()).getId();
        Long zombieId = entidadRepository.save(new Zombie("Zombie", 30)).getId();

        ExplosionResultado resultado = service.simularExplosion(new ExplosionRequest(
                creeperId, true,
                List.of(new VictimaRequest(zombieId, 0.0))));

        assertTrue(resultado.creeperCargado());
        assertEquals(25, resultado.danioBase());
        assertTrue(resultado.creeperDestruido());
    }
}
