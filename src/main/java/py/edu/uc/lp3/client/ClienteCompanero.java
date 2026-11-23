package py.edu.uc.lp3.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

import py.edu.uc.lp3.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.exception.CompaneroRechazoException;

import reactor.core.publisher.Mono;

/**
 * Logica comun de los clientes HTTP de los companeros: envia el cuerpo,
 * conserva la respuesta exitosa como JsonNode y traduce los rechazos 4xx o
 * las fallas de conexion a excepciones de dominio.
 */
abstract class ClienteCompanero {

    private final String origen;
    private final WebClient webClient;
    private final String explosionPath;
    private final ObjectMapper objectMapper;

    protected ClienteCompanero(String origen, WebClient webClient,
                               String explosionPath, ObjectMapper objectMapper) {
        this.origen = origen;
        this.webClient = webClient;
        this.explosionPath = explosionPath;
        this.objectMapper = objectMapper;
    }

    protected JsonNode enviarExplosion(Object cuerpo) {
        try {
            Respuesta respuesta = webClient.post()
                    .uri(explosionPath)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(cuerpo)
                    .exchangeToMono(this::leer)
                    .block();

            if (respuesta == null) {
                throw new CompaneroNoDisponibleException(origen);
            }
            return interpretar(respuesta);
        } catch (WebClientRequestException ex) {
            throw new CompaneroNoDisponibleException(origen, ex);
        }
    }

    private Mono<Respuesta> leer(ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(texto -> new Respuesta(response.statusCode(), texto));
    }

    private JsonNode interpretar(Respuesta respuesta) {
        HttpStatusCode status = respuesta.status();
        if (status.is2xxSuccessful()) {
            return leerJson(respuesta.cuerpo());
        }
        if (status.is4xxClientError()) {
            JsonNode error = leerJson(respuesta.cuerpo());
            throw new CompaneroRechazoException(origen, status.value(),
                    errorCompanero(error), mensaje(error));
        }
        throw new CompaneroNoDisponibleException(origen);
    }

    private JsonNode leerJson(String texto) {
        if (texto == null || texto.isBlank()) {
            return NullNode.instance;
        }
        try {
            return objectMapper.readTree(texto);
        } catch (JsonProcessingException ex) {
            return NullNode.instance;
        }
    }

    private String errorCompanero(JsonNode error) {
        String tipo = textoDe(error, "tipo");
        return tipo != null ? tipo : textoDe(error, "error");
    }

    private String mensaje(JsonNode error) {
        String mensaje = textoDe(error, "mensaje");
        return mensaje != null ? mensaje
                : "El companero " + origen + " rechazo la simulacion";
    }

    private String textoDe(JsonNode nodo, String campo) {
        JsonNode valor = nodo == null ? null : nodo.get(campo);
        return valor == null || valor.isNull() ? null : valor.asText();
    }

    private record Respuesta(HttpStatusCode status, String cuerpo) {
    }
}
