package py.edu.uc.lp3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import py.edu.uc.lp3.domain.Creeper;
import py.edu.uc.lp3.exception.EntidadInvalidaException;
import py.edu.uc.lp3.exception.EstadoInvalidoException;
import py.edu.uc.lp3.repository.EntidadRepository;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilRequest;
import py.edu.uc.lp3.rest.dto.HostilDTO.HostilResponse;

@DataJpaTest
@Import(HostilService.class)
class HostilServiceTest {

    @Autowired
    private HostilService service;

    @Autowired
    private EntidadRepository repository;

    @Test
    void creaUnCreeperConNombreYVidaUsandoSuConstructor() {
        HostilResponse respuesta = service.crear(new HostilRequest(
                "creeper", "Boom", 5, null, null, null, null, null));

        assertEquals("creeper", respuesta.tipo());
        assertEquals("Boom", respuesta.nombre());
        assertEquals(5, respuesta.vida());
    }

    @Test
    void validaLosDatosDeEntradaEnElService() {
        assertThrows(EntidadInvalidaException.class, () -> service.crear(new HostilRequest(
                "zombie", "  ", 20, 0.2, 16.0, 3, null, null)));
        assertThrows(EntidadInvalidaException.class, () -> service.crear(new HostilRequest(
                "zombie", "Zombie", 0, 0.2, 16.0, 3, null, null)));
        assertThrows(EntidadInvalidaException.class, () -> service.crear(new HostilRequest(
                "zombie", "Zombie", 20, -1.0, 16.0, 3, null, null)));
        assertThrows(EntidadInvalidaException.class, () -> service.crear(new HostilRequest(
                "zombie", "Zombie", 20, 0.2, 0.0, 3, null, null)));
        assertThrows(EntidadInvalidaException.class, () -> service.crear(new HostilRequest(
                "zombie", "Zombie", 20, 0.2, 16.0, 0, null, null)));
        assertThrows(EntidadInvalidaException.class, () -> service.crear(new HostilRequest(
                "dragon", "Dragon", 20, 0.2, 16.0, 3, null, null)));
    }

    @Test
    void unMobMuertoNoPuedeRevivirConPatch() {
        Long id = repository.save(new Creeper("Muerto", 0)).getId();

        assertThrows(EstadoInvalidoException.class, () -> service.actualizarParcial(
                id, new HostilRequest(null, null, 20, null, null, null, null, null)));
    }

    @Test
    void unMobMuertoNoPuedeRevivirConPut() {
        Long id = repository.save(new Creeper("Muerto", 0)).getId();

        assertThrows(EstadoInvalidoException.class, () -> service.actualizarTotal(
                id, new HostilRequest("creeper", "Nuevo", 20, 0.2, 16.0, 15, null, null)));
    }
}
