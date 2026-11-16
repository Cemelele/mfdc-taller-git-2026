package py.edu.uc.lp3.domain;

import jakarta.persistence.Embeddable;

/**
 * Clase auxiliar: el hitbox fisico de una entidad (ancho x alto).
 * Es una relacion de composicion: toda Entidad "tiene un" Hitbox.
 *
 * Se embebe en la tabla de la entidad. El protected vacio existe solo
 * para que JPA pueda reconstruir el valor embebido desde la fila.
 */
@Embeddable
public class Hitbox {

    private double ancho;
    private double alto;

    protected Hitbox() {
        this.ancho = 1.0;
        this.alto = 1.0;
    }

    public Hitbox(double ancho, double alto) {
        this.ancho = ancho;
        this.alto = alto;
    }

    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }

    @Override
    public String toString() {
        return ancho + " x " + alto;
    }
}
