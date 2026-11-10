package py.edu.uc.lp3.rest.dto;

import java.util.List;

public final class SimulacionDTO {

    private SimulacionDTO() {}

    public record Victima(String tipo, Double distancia) {}

    public record ExplosionRequest(Boolean cargado, List<Victima> victimas) {}

    public record VictimaResultado(String tipo, double distancia, int danio,
                                   int vidaFinal, boolean viva, String detalle) {}

    public record ExplosionResultado(boolean creeperCargado, double radio,
                                     int danioBase, List<VictimaResultado> victimas) {}
}