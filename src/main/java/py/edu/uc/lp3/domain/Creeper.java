package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("creeper")
public class Creeper extends Hostil implements Electrificable {

    private static final int DANIO_BASE = 15;
    private static final int DANIO_CARGADO = 25;

    private double radioExplosion;
    private boolean cargado;
    private boolean encendido;

    public Creeper() {
        super("Creeper", 20, new Hitbox(0.6, 1.7), "Polvora",
                "texturas/creeper.png", "siseo", 0.25, 16, DANIO_BASE);
        this.radioExplosion = 3.0;
        this.cargado = false;
        this.encendido = false;
    }

    /** El Creeper no golpea: se acerca, sisea y explota. */
    @Override
    public String accionAtaque(Jugador objetivo) {
        reproducirSonido("fuse");
        encender();
        int danio = danioBase();
        String resultado = "El Creeper sisea y explota en un radio de " + radioExplosion
                + " bloques" + (cargado ? ", cargado por un rayo (" + danio + " de dano)" : "")
                + ". " + objetivo.recibirDanio(danio);
        return resultado + ". " + explotar(); // la explosion lo destruye a el tambien
    }

    /** Dano maximo de la explosion: el que recibe quien esta pegado al Creeper. */
    public int danioBase() {
        return cargado ? DANIO_CARGADO : DANIO_BASE;
    }

    /** Regla 1: el dano baja linealmente con la distancia y es 0 fuera del radio. */
    public int danioA(double distancia) {
        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa");
        }
        if (distancia >= radioExplosion) {
            return 0;
        }
        return (int) Math.round(danioBase() * (1 - distancia / radioExplosion));
    }

    /** Enciende la mecha: queda en cuenta regresiva antes de detonar. */
    public String encender() {
        if (estaMuerto()) {
            return getNombre() + " ya murio, no puede encender su mecha.";
        }
        this.encendido = true;
        return getNombre() + " enciende su mecha y sisea antes de explotar.";
    }

    /**
     * Regla 2: un Creeper cargado por un rayo explota con dano base mayor y
     * la explosion lo destruye a el tambien. La simulacion llama a este
     * mensaje para dejar constancia de que el Creeper ya no esta vivo.
     */
    public String explotar() {
        this.encendido = true;
        return morir();
    }

    public String cambiarRadioExplosion(double nuevoRadio) {
        if (nuevoRadio <= 0) {
            throw new IllegalArgumentException("El radio de explosion debe ser positivo");
        }
        this.radioExplosion = nuevoRadio;
        return getNombre() + " cambia su radio de explosion a " + nuevoRadio + " bloques.";
    }

    @Override
    public String recibirRayo() {
        this.cargado = true;
        return "Creeper cargado por un rayo: ahora explota mas fuerte.";
    }

    /** Reemplaza el default de Electrificable con un aviso propio del Creeper. */
    @Override
    public String avisoRayo() {
        return "El Creeper sisea mas fuerte mientras el rayo lo carga.";
    }

    public boolean isCargado() { return cargado; }
    public boolean isEncendido() { return encendido; }
    public double getRadioExplosion() { return radioExplosion; }
}
