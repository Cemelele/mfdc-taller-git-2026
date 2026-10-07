package py.edu.uc.lp3.domain;

/**
 * Entidades que pueden atacar al jugador.
 *
 * accionAtaque() es abstracto: cada mob hostil ataca de forma distinta
 * (esto es polimorfismo: misma llamada, comportamiento diferente).
 *
 * comportamiento() queda resuelto aqui y es final: un hostil siempre
 * se comporta asi, la diferencia la aporta cada hija en accionAtaque().
 */
public abstract class Hostil extends Entidad {

    private final double rangoAggro;

    protected Hostil(String nombre, int vida, Hitbox hitboxFisico, String drop, double rangoAggro) {
        super(nombre, vida, hitboxFisico, drop);
        if (rangoAggro <= 0) {
            throw new IllegalArgumentException("El rango de aggro de " + nombre + " debe ser positivo");
        }
        this.rangoAggro = rangoAggro;
    }

    /**
     * Un hostil se activa atacando. El mensaje comun lo arma esta clase;
     * lo que ataquen es responsabilidad de cada hija.
     */
    @Override
    public final String comportamiento(Jugador objetivo) {
        return estaMuerto()
                ? getNombre() + " ya murio."
                : accionAtaque(objetivo);
    }

    /** Cada subclase define como ataca. */
    public abstract String accionAtaque(Jugador objetivo);

    /** Comportamiento comun: detectar al jugador dentro del rango de aggro. */
    public String detecta(Jugador objetivo, double distancia) {
        if (distancia < 0) {
            throw new IllegalArgumentException("La distancia no puede ser negativa");
        }
        boolean detectado = distancia <= rangoAggro;
        return getNombre() + (detectado ? " detecta a " : " no detecta a ")
                + objetivo.getNombre() + ".";
    }

    public double getRangoAggro() { return rangoAggro; }
}
