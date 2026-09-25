package minecraft;

/**
 * Clase auxiliar: el hitbox físico de una entidad (ancho x alto).
 * Es una relación de composición: toda Entidad "tiene un" Hitbox.
 */
public class Hitbox {

    private final double ancho;
    private final double alto;

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
