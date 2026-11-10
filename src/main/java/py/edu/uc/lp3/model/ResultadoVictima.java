package py.edu.uc.lp3.model;

import jakarta.persistence.Embeddable;

/**
 * Resultado de una victima dentro de una simulacion de explosion.
 * Es un valor embebido: no tiene identidad propia, vive en la coleccion
 * de la simulacion que lo genero.
 */
@Embeddable
public class ResultadoVictima {

    private Long hostilId;
    private String tipo;
    private double distancia;
    private int danio;
    private int vidaAntes;
    private int vidaDespues;
    private boolean viva;
    private String detalle;

    protected ResultadoVictima() {
    }

    public ResultadoVictima(Long hostilId, String tipo, double distancia, int danio,
                            int vidaAntes, int vidaDespues, boolean viva, String detalle) {
        this.hostilId = hostilId;
        this.tipo = tipo;
        this.distancia = distancia;
        this.danio = danio;
        this.vidaAntes = vidaAntes;
        this.vidaDespues = vidaDespues;
        this.viva = viva;
        this.detalle = detalle;
    }

    public Long getHostilId() { return hostilId; }
    public String getTipo() { return tipo; }
    public double getDistancia() { return distancia; }
    public int getDanio() { return danio; }
    public int getVidaAntes() { return vidaAntes; }
    public int getVidaDespues() { return vidaDespues; }
    public boolean isViva() { return viva; }
    public String getDetalle() { return detalle; }
}
