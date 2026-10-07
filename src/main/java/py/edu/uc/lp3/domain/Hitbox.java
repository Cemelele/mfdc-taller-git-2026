package py.edu.uc.lp3.domain;

/**
 * Clase auxiliar: el hitbox fisico de una entidad (ancho x alto).
 * Es una relacion de composicion: toda Entidad "tiene un" Hitbox.
 *
 * Inmutable: una vez creado no se puede deformar desde afuera.
 */
public final class Hitbox {

    private final double ancho;
    private final double alto;

    public Hitbox(double ancho, double alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("Las dimensiones del hitbox deben ser positivas");
        }
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
