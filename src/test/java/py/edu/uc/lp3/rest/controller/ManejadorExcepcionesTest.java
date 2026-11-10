package py.edu.uc.lp3.rest.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ManejadorExcepcionesTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void unaEntidadInexistenteDevuelve404ConJSONEstructurado() throws Exception {
        mvc.perform(get("/api/hostiles/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.tipo").value("EntidadNoEncontradaException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("999999")));
    }

    @Test
    void unCreeperInexistenteDevuelve404ConSuExcepcionEspecifica() throws Exception {
        String cuerpo = """
                {
                  "creeperId": 999999,
                  "victimas": [ { "hostilId": 1, "distancia": 0 } ]
                }
                """;

        mvc.perform(post("/api/simulaciones/explosion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.tipo").value("CreeperNoEncontradoException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("999999")));
    }

    @Test
    void unaEntidadInvalidaDevuelve400ConElMotivoDelDominio() throws Exception {
        String cuerpo = """
                {
                  "tipo": "zombie",
                  "nombre": "Zombie invalido",
                  "vida": -5,
                  "velocidad": 0.2
                }
                """;

        mvc.perform(post("/api/hostiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.tipo").value("EntidadInvalidaException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("vida")));
    }

    @Test
    void unaSimulacionInvalidaDevuelve400ConSuExcepcionEspecifica() throws Exception {
        String cuerpo = """
                {
                  "creeperId": 1,
                  "victimas": []
                }
                """;

        mvc.perform(post("/api/simulaciones/explosion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.tipo").value("SimulacionInvalidaException"))
                .andExpect(jsonPath("$.mensaje").value(containsString("victima")));
    }
}
