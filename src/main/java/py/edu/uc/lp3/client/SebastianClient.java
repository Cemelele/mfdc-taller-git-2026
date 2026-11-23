package py.edu.uc.lp3.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import py.edu.uc.lp3.client.dto.ExplosionSebastianRequest;

@Component
public class SebastianClient extends ClienteCompanero {

    private static final String ORIGEN = "A3";

    public SebastianClient(@Qualifier("sebastianWebClient") WebClient webClient,
                           @Value("${companeros.a3.explosion-path}") String explosionPath,
                           ObjectMapper objectMapper) {
        super(ORIGEN, webClient, explosionPath, objectMapper);
    }

    public JsonNode simularExplosion(Long creeperId, Long jugadorId,
                                     Long zombieId, Integer radio) {
        return enviarExplosion(new ExplosionSebastianRequest(
                creeperId, jugadorId, zombieId, radio));
    }
}
