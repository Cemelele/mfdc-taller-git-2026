package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("creeper")
public class Creeper extends Hostil implements Electrificable {

    private double radioExplosion;
    private boolean cargado;
    private boolean encendido;

    public Creeper() {
        this("Creeper", 20);
    }

    public Creeper(String nombre, int vida) {
        super(nombre, vida, new Hitbox(0.6, 1.7), "Polvora",
                "texturas/creeper.png", "siseo", 0.25, 16, 15);
        this.radioExplosion = 3.0;
        this.cargado = false;
        this.encendido = false;
    }

    /** El Creeper no golpea: se acerca, sisea y explota. */
    @Override
    public String accionAtaque(Jugador objetivo) {
        reproducirSonido("fuse");
        encender();
        return "El Creeper sisea y explota en un radio de " + radioExplosion
                + " bloques. " + objetivo.recibirDanio(getDanioAtaque());
    }

    /** Enciende la mecha: queda en cuenta regresiva antes de detonar. */
    public String encender() {
        this.encendido = true;
        return getNombre() + " enciende su mecha y sisea antes de explotar.";
    }

    public String cambiarRadioExplosion(double nuevoRadio) {
        this.radioExplosion = nuevoRadio;
        return getNombre() + " cambia su radio de explosion a " + nuevoRadio + " bloques.";
    }

    /** Mutacion simple del estado: el Creeper queda cargado. */
    public void cargar() {
        this.cargado = true;
    }

    @Override
    public String recibirRayo() {
        cargar();
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
