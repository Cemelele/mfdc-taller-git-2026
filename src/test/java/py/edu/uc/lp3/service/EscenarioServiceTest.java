package py.edu.uc.lp3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import py.edu.uc.lp3.client.RuthClient;
import py.edu.uc.lp3.client.SebastianClient;
import py.edu.uc.lp3.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.exception.CompaneroRechazoException;
import py.edu.uc.lp3.model.EscenarioCreeperElectrificado;
import py.edu.uc.lp3.repository.EscenarioCreeperElectrificadoRepository;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.A2Request;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.A3Request;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.CreeperElectrificadoRequest;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.EscenarioGuardado;

@DataJpaTest
class EscenarioServiceTest {

    @Autowired
    private EscenarioCreeperElectrificadoRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private RuthClient ruthClient;
    private SebastianClient sebastianClient;
    private EscenarioService service;

    @BeforeEach
    void preparar() {
        ruthClient = mock(RuthClient.class);
        sebastianClient = mock(SebastianClient.class);
        service = new EscenarioService(ruthClient, sebastianClient, repository);
    }

    @Test
    void guardaLaJugadaCuandoA2YA3Aceptan() {
        when(ruthClient.simularExplosion(10L)).thenReturn(json("{\"aceptada\":true}"));
        when(sebastianClient.simularExplosion(10L, 20L, 30L, 4))
                .thenReturn(json("{\"status\":\"ok\"}"));

        EscenarioGuardado guardado = service.crear(peticion());

        assertNotNull(guardado.id());
        assertEquals("Creeper electrificado", guardado.descripcion());
        assertEquals(10L, guardado.a2().creeperId());
        assertEquals(10L, guardado.a3().creeperId());
        assertEquals(20L, guardado.a3().jugadorId());
        assertEquals(30L, guardado.a3().zombieId());
        assertEquals(4, guardado.a3().radio());
        assertTrue(guardado.respuestaA2().contains("aceptada"));
        assertTrue(guardado.respuestaA3().contains("ok"));
        assertEquals(1, repository.count());

        EscenarioCreeperElectrificado almacenado = repository.findById(guardado.id()).orElseThrow();
        assertEquals(10L, almacenado.getA2CreeperId());
        assertEquals(20L, almacenado.getA3JugadorId());
        verify(ruthClient, times(1)).simularExplosion(10L);
        verify(sebastianClient, times(1)).simularExplosion(10L, 20L, 30L, 4);
    }

    @Test
    void siA2RechazaNoLlamaA3YNoGuardaFilas() {
        when(ruthClient.simularExplosion(10L))
                .thenThrow(new CompaneroRechazoException("A2", 409,
                        "EstadoInvalido", "el creeper ya exploto"));

        CompaneroRechazoException ex = assertThrows(CompaneroRechazoException.class,
                () -> service.crear(peticion()));

        assertEquals("A2", ex.getOrigen());
        assertEquals(409, ex.getStatus());
        assertEquals(0, repository.count());
        verifyNoInteractions(sebastianClient);
    }

    @Test
    void siA3RechazaNoGuardaFilas() {
        when(ruthClient.simularExplosion(10L)).thenReturn(json("{\"aceptada\":true}"));
        when(sebastianClient.simularExplosion(10L, 20L, 30L, 4))
                .thenThrow(new CompaneroRechazoException("A3", 404,
                        "Not Found", "no existe el jugador"));

        CompaneroRechazoException ex = assertThrows(CompaneroRechazoException.class,
                () -> service.crear(peticion()));

        assertEquals("A3", ex.getOrigen());
        assertEquals(404, ex.getStatus());
        assertEquals(0, repository.count());
    }

    @Test
    void siA2NoRespondeNoLlamaA3YNoGuardaFilas() {
        when(ruthClient.simularExplosion(10L))
                .thenThrow(new CompaneroNoDisponibleException("A2"));

        CompaneroNoDisponibleException ex = assertThrows(CompaneroNoDisponibleException.class,
                () -> service.crear(peticion()));

        assertEquals("A2", ex.getOrigen());
        assertEquals(0, repository.count());
        verifyNoInteractions(sebastianClient);
    }

    @Test
    void siA3NoRespondeNoGuardaFilas() {
        when(ruthClient.simularExplosion(10L)).thenReturn(json("{\"aceptada\":true}"));
        when(sebastianClient.simularExplosion(10L, 20L, 30L, 4))
                .thenThrow(new CompaneroNoDisponibleException("A3"));

        CompaneroNoDisponibleException ex = assertThrows(CompaneroNoDisponibleException.class,
                () -> service.crear(peticion()));

        assertEquals("A3", ex.getOrigen());
        assertEquals(0, repository.count());
    }

    private CreeperElectrificadoRequest peticion() {
        return new CreeperElectrificadoRequest("Creeper electrificado",
                new A2Request(10L),
                new A3Request(10L, 20L, 30L, 4));
    }

    private JsonNode json(String texto) {
        try {
            return objectMapper.readTree(texto);
        } catch (Exception ex) {
            throw new IllegalArgumentException("JSON de prueba invalido", ex);
        }
    }
}
