package py.edu.uc.lp3.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

/**
 * Simulacion persistida de una explosion de Creeper: guarda quien exploto,
 * con que reglas y el resultado de cada victima. El GET posterior devuelve
 * este registro y el estado de los Hostil que participaron.
 */
@Entity
public class SimulacionExplosion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Long creeperId;
    private boolean creeperCargado;
    private boolean creeperEncendido;
    private double radio;
    private int danioBase;
    private boolean creeperDestruido;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "simulacion_victima", joinColumns = @JoinColumn(name = "simulacion_id"))
    private List<ResultadoVictima> victimas;

    protected SimulacionExplosion() {
        this.victimas = new ArrayList<>();
    }

    public SimulacionExplosion(Long creeperId, boolean creeperCargado,
                               double radio, int danioBase) {
        this(creeperId, creeperCargado, true, radio, danioBase);
    }

    public SimulacionExplosion(Long creeperId, boolean creeperCargado,
                               boolean creeperEncendido, double radio, int danioBase) {
        this.fecha = LocalDateTime.now();
        this.creeperId = creeperId;
        this.creeperCargado = creeperCargado;
        this.creeperEncendido = creeperEncendido;
        this.radio = radio;
        this.danioBase = danioBase;
        this.creeperDestruido = false;
        this.victimas = new ArrayList<>();
    }

    public void agregarResultado(ResultadoVictima resultado) {
        this.victimas.add(resultado);
    }

    public void marcarCreeperDestruido() {
        this.creeperDestruido = true;
    }

    public Long getId() { return id; }
    public LocalDateTime getFecha() { return fecha; }
    public Long getCreeperId() { return creeperId; }
    public boolean isCreeperCargado() { return creeperCargado; }
    public boolean isCreeperEncendido() { return creeperEncendido; }
    public double getRadio() { return radio; }
    public int getDanioBase() { return danioBase; }
    public boolean isCreeperDestruido() { return creeperDestruido; }
    public List<ResultadoVictima> getVictimas() { return victimas; }
}
