package py.edu.uc.lp3.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import py.edu.uc.lp3.client.RuthClient;
import py.edu.uc.lp3.client.SebastianClient;
import py.edu.uc.lp3.exception.EscenarioInvalidoException;
import py.edu.uc.lp3.model.EscenarioCreeperElectrificado;
import py.edu.uc.lp3.repository.EscenarioCreeperElectrificadoRepository;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.A2Request;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.A3Request;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.CreeperElectrificadoRequest;
import py.edu.uc.lp3.rest.dto.EscenarioDTO.EscenarioGuardado;

/**
 * Orquesta el escenario de Creeper electrificado. Las llamadas a A2 y A3
 * ocurren fuera de una transaccion: solo despues de que ambos aceptan se
 * abre la escritura del repositorio para guardar la jugada.
 */
@Service
public class EscenarioService {

    private final RuthClient ruthClient;
    private final SebastianClient sebastianClient;
    private final EscenarioCreeperElectrificadoRepository repository;

    public EscenarioService(RuthClient ruthClient,
                            SebastianClient sebastianClient,
                            EscenarioCreeperElectrificadoRepository repository) {
        this.ruthClient = ruthClient;
        this.sebastianClient = sebastianClient;
        this.repository = repository;
    }

    public EscenarioGuardado crear(CreeperElectrificadoRequest peticion) {
        validar(peticion);

        A2Request a2 = peticion.a2();
        A3Request a3 = peticion.a3();

        JsonNode respuestaA2 = ruthClient.simularExplosion(a2.creeperId());
        JsonNode respuestaA3 = sebastianClient.simularExplosion(
                a3.creeperId(), a3.jugadorId(), a3.zombieId(), a3.radio());

        EscenarioCreeperElectrificado guardado = repository.save(
                new EscenarioCreeperElectrificado(peticion.descripcion(),
                        a2.creeperId(),
                        a3.creeperId(), a3.jugadorId(), a3.zombieId(), a3.radio(),
                        texto(respuestaA2), texto(respuestaA3)));

        return mapear(guardado);
    }

    public List<EscenarioGuardado> listar() {
        return repository.findAll().stream().map(this::mapear).toList();
    }

    private void validar(CreeperElectrificadoRequest peticion) {
        if (peticion == null) {
            throw new EscenarioInvalidoException("La peticion del escenario no puede estar vacia");
        }
        if (peticion.descripcion() == null || peticion.descripcion().isBlank()) {
            throw new EscenarioInvalidoException("Debe indicar una descripcion del escenario");
        }
        if (peticion.a2() == null || peticion.a2().creeperId() == null) {
            throw new EscenarioInvalidoException("Debe indicar el creeperId de A2");
        }
        A3Request a3 = peticion.a3();
        if (a3 == null) {
            throw new EscenarioInvalidoException("Debe indicar los datos de A3");
        }
        if (a3.creeperId() == null || a3.jugadorId() == null
                || a3.zombieId() == null || a3.radio() == null) {
            throw new EscenarioInvalidoException(
                    "A3 necesita creeperId, jugadorId, zombieId y radio");
        }
        if (a3.radio() <= 0) {
            throw new EscenarioInvalidoException("El radio de A3 debe ser positivo");
        }
    }

    private String texto(JsonNode respuesta) {
        return respuesta == null ? "null" : respuesta.toString();
    }

    private EscenarioGuardado mapear(EscenarioCreeperElectrificado escenario) {
        return new EscenarioGuardado(escenario.getId(), escenario.getDescripcion(),
                new A2Request(escenario.getA2CreeperId()),
                new A3Request(escenario.getA3CreeperId(), escenario.getA3JugadorId(),
                        escenario.getA3ZombieId(), escenario.getA3Radio()),
                escenario.getRespuestaA2(), escenario.getRespuestaA3(),
                escenario.getFecha());
    }
}
