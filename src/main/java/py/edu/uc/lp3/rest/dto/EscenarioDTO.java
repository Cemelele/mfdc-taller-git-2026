package py.edu.uc.lp3.rest.dto;

import java.time.LocalDateTime;

public final class EscenarioDTO {

    private EscenarioDTO() {
    }

    public record A2Request(Long creeperId) {
    }

    public record A3Request(Long creeperId, Long jugadorId, Long zombieId, Integer radio) {
    }

    public record CreeperElectrificadoRequest(String descripcion,
                                              A2Request a2,
                                              A3Request a3) {
    }

    public record EscenarioGuardado(Long id, String descripcion,
                                    A2Request a2, A3Request a3,
                                    String respuestaA2, String respuestaA3,
                                    LocalDateTime fecha) {
    }
}
