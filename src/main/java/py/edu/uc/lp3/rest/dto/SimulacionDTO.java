package py.edu.uc.lp3.rest.dto;

import java.util.List;

public final class SimulacionDTO {

    private SimulacionDTO() {
    }

    public record VictimaRequest(Long hostilId, Double distancia) {
    }

    public record ExplosionRequest(Long creeperId, Boolean cargado,
                                   List<VictimaRequest> victimas) {
    }

    public record VictimaResultado(Long hostilId, String tipo, double distancia,
                                   int danio, int vidaAntes, int vidaDespues,
                                   boolean viva, String detalle) {
    }

    public record ExplosionResultado(Long simulacionId, Long creeperId,
                                     boolean creeperCargado, double radio, int danioBase,
                                     boolean creeperDestruido,
                                     List<VictimaResultado> victimas) {
    }
}
