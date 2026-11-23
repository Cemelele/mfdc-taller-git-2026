package py.edu.uc.lp3.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import py.edu.uc.lp3.client.dto.ExplosionRuthRequest;

@Component
public class RuthClient extends ClienteCompanero {

    private static final String ORIGEN = "A2";

    public RuthClient(@Qualifier("ruthWebClient") WebClient webClient,
                      @Value("${companeros.a2.explosion-path}") String explosionPath,
                      ObjectMapper objectMapper) {
        super(ORIGEN, webClient, explosionPath, objectMapper);
    }

    public JsonNode simularExplosion(Long creeperId) {
        return enviarExplosion(new ExplosionRuthRequest(creeperId));
    }
}
