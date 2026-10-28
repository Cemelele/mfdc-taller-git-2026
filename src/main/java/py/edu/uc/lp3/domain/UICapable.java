package py.edu.uc.lp3.domain;

/**
 * Capacidad de todo elemento dibujable del mundo: sabe con que imagen
 * se dibuja, donde esta, que sonido emite y a que velocidad se mueve.
 *
 * La implementa {@link Entidad}, asi que todas las entidades (hostiles,
 * pasivas y el jugador) son dibujables. El que pide estos datos habla
 * con el tipo de la interfaz, sin conocer la clase concreta.
 */
public interface UICapable {

    /** Ruta de la textura o sprite con el que se dibuja el elemento. */
    String getImagen();

    /** Posicion en el mundo, lista para el render. */
    String getPosicion();

    /** Sonido caracteristico que emite el elemento. */
    String getSonido();

    /** Velocidad de desplazamiento, en bloques por tick. */
    double getVelocidad();
}
