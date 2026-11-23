package py.edu.uc.lp3.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * Jugada guardada cuando A2 y A3 aceptan la simulacion de un Creeper
 * electrificado. Las respuestas se conservan como texto JSON porque cada
 * companero tiene su propio contrato.
 */
@Entity
@Table(name = "escenario_creeper_electrificado")
public class EscenarioCreeperElectrificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private Long a2CreeperId;

    private Long a3CreeperId;
    private Long a3JugadorId;
    private Long a3ZombieId;
    private Integer a3Radio;

    @Lob
    private String respuestaA2;

    @Lob
    private String respuestaA3;

    private LocalDateTime fecha;

    protected EscenarioCreeperElectrificado() {
    }

    public EscenarioCreeperElectrificado(String descripcion,
                                         Long a2CreeperId,
                                         Long a3CreeperId,
                                         Long a3JugadorId,
                                         Long a3ZombieId,
                                         Integer a3Radio,
                                         String respuestaA2,
                                         String respuestaA3) {
        this.descripcion = descripcion;
        this.a2CreeperId = a2CreeperId;
        this.a3CreeperId = a3CreeperId;
        this.a3JugadorId = a3JugadorId;
        this.a3ZombieId = a3ZombieId;
        this.a3Radio = a3Radio;
        this.respuestaA2 = respuestaA2;
        this.respuestaA3 = respuestaA3;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public Long getA2CreeperId() { return a2CreeperId; }
    public Long getA3CreeperId() { return a3CreeperId; }
    public Long getA3JugadorId() { return a3JugadorId; }
    public Long getA3ZombieId() { return a3ZombieId; }
    public Integer getA3Radio() { return a3Radio; }
    public String getRespuestaA2() { return respuestaA2; }
    public String getRespuestaA3() { return respuestaA3; }
    public LocalDateTime getFecha() { return fecha; }
}
