package py.edu.uc.lp3.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import io.netty.channel.ChannelOption;
import py.edu.uc.lp3.exception.CompaneroNoDisponibleException;
import py.edu.uc.lp3.exception.CompaneroRechazoException;
import reactor.netty.http.client.HttpClient;

class SebastianClientTest {

    private static final String PATH = "/minecraft/sgomezdelafuente/simulaciones/explosion";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicReference<String> cuerpoRecibido = new AtomicReference<>();
    private final AtomicReference<String> rutaRecibida = new AtomicReference<>();
    private final AtomicReference<String> respuesta = new AtomicReference<>("{}");
    private final AtomicInteger status = new AtomicInteger(200);
    private final AtomicLong demoraMs = new AtomicLong();

    private HttpServer server;
    private SebastianClient client;

    @BeforeEach
    void levantarServidor() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext(PATH, this::atender);
        server.start();

        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:" + server.getAddress().getPort())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                        .responseTimeout(Duration.ofSeconds(2))))
                .build();
        client = new SebastianClient(webClient, PATH, objectMapper);
    }

    @AfterEach
    void detenerServidor() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void enviaLosCuatroIdsYDevuelveLaRespuestaComoJson() throws Exception {
        status.set(200);
        respuesta.set("{\"aceptada\":true,\"detalle\":\"ok\"}");

        JsonNode resultado = client.simularExplosion(7L, 11L, 13L, 4);

        JsonNode enviado = objectMapper.readTree(cuerpoRecibido.get());
        assertEquals(7L, enviado.path("creeperId").asLong());
        assertEquals(11L, enviado.path("jugadorId").asLong());
        assertEquals(13L, enviado.path("zombieId").asLong());
        assertEquals(4, enviado.path("radio").asInt());
        assertEquals(PATH, rutaRecibida.get());
        assertTrue(resultado.path("aceptada").asBoolean());
    }

    @ParameterizedTest
    @ValueSource(ints = {404, 409})
    void unRechazo4xxLanzaCompaneroRechazo(int codigo) {
        status.set(codigo);
        respuesta.set("""
                {"timestamp":"2026-11-23T10:00:00","status":%d,\
                "error":"Not Found","mensaje":"no existe el creeper","path":"%s"}
                """.formatted(codigo, PATH));

        CompaneroRechazoException ex = assertThrows(CompaneroRechazoException.class,
                () -> client.simularExplosion(3L, 5L, 6L, 2));

        assertEquals("A3", ex.getOrigen());
        assertEquals(codigo, ex.getStatus());
        assertEquals("Not Found", ex.getCompaneroError());
        assertEquals("no existe el creeper", ex.getMessage());
    }

    @Test
    void unTimeoutLanzaCompaneroNoDisponible() {
        demoraMs.set(4000);
        respuesta.set("{\"aceptada\":true}");

        CompaneroNoDisponibleException ex = assertThrows(CompaneroNoDisponibleException.class,
                () -> client.simularExplosion(1L, 2L, 3L, 1));

        assertEquals("A3", ex.getOrigen());
    }

    @Test
    void unServidorCaidoLanzaCompaneroNoDisponible() {
        server.stop(0);
        server = null;

        CompaneroNoDisponibleException ex = assertThrows(CompaneroNoDisponibleException.class,
                () -> client.simularExplosion(1L, 2L, 3L, 1));

        assertEquals("A3", ex.getOrigen());
    }

    private void atender(HttpExchange intercambio) throws IOException {
        cuerpoRecibido.set(new String(intercambio.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8));
        rutaRecibida.set(intercambio.getRequestURI().getPath());
        esperar();
        byte[] bytes = respuesta.get().getBytes(StandardCharsets.UTF_8);
        intercambio.getResponseHeaders().add("Content-Type", "application/json");
        intercambio.sendResponseHeaders(status.get(), bytes.length);
        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(bytes);
        }
    }

    private void esperar() {
        long demora = demoraMs.get();
        if (demora <= 0) {
            return;
        }
        try {
            Thread.sleep(demora);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
