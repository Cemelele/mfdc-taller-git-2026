package py.edu.uc.lp3.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Comportamiento observable del servicio: el IndexController responde en
 * GET /, el EntidadController construye desde la URL y devuelve JSON, y
 * las reglas del dominio se aplican aunque el pedido llegue por HTTP.
 */
@SpringBootTest
@AutoConfigureMockMvc
class EntidadControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void indexRespondeQueElServicioEstaVivo() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Minecraft")));
    }

    @Test
    void construyeUnaEntidadDesdeLaURLYRespondeElJSONDeLaClaseHija() throws Exception {
        mvc.perform(get("/entidad/zombie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comportamiento").value(
                        containsString("golpea cuerpo a cuerpo")))
                .andExpect(jsonPath("$.vidaFinal").value(20));
    }

    @Test
    void laVidaDeLaURLAlimentaUnConstructorSobrecargado() throws Exception {
        mvc.perform(get("/entidad/zombie?vida=35"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vidaFinal").value(35));
    }

    @Test
    void elAtacanteDeLaURLEligeLaSobrecargaDeRecibirDanio() throws Exception {
        mvc.perform(get("/entidad/esqueleto?danio=5&atacante=Creeper"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recibirDanio").value(
                        containsString("Creeper ataca a Esqueleto")))
                .andExpect(jsonPath("$.vidaFinal").value(15));
    }

    @Test
    void elAldeanoSinProfesionUsaElConstructorSimple() throws Exception {
        mvc.perform(get("/entidad/aldeano?profesion="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comportamiento").value(
                        containsString("Granjero")));
    }

    @Test
    void laClaseRechazaUnDanoNegativoYElControllerResponde400() throws Exception {
        mvc.perform(get("/entidad/creeper?danio=-5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(
                        containsString("no puede ser negativo")));
    }

    @Test
    void listaTodasLasEntidadesCadaUnaConSuComportamiento() throws Exception {
        mvc.perform(get("/entidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    void tipoDesconocidoResponde404() throws Exception {
        mvc.perform(get("/entidad/dragon"))
                .andExpect(status().isNotFound());
    }
}
