package minecraft;

/**
 * Entidades que pueden atacar al jugador.
 * accionAtaque() es abstracto: cada mob hostil ataca de forma distinta
 * (esto es polimorfismo: misma llamada, comportamiento diferente).
 */
public abstract class Hostil extends Entidad {

    protected double rangoAggro;

    public Hostil(String nombre, int vida, Hitbox hitboxFisico, String drop, double rangoAggro) {
        super(nombre, vida, hitboxFisico, drop);
        this.rangoAggro = rangoAggro;
    }

    /** Cada subclase define cómo ataca. */
    public abstract void accionAtaque(Jugador objetivo);

    /** Comportamiento común: detectar al jugador dentro del rango de aggro. */
    public boolean detecta(Jugador objetivo, double distancia) {
        boolean detectado = distancia <= rangoAggro;
        System.out.println(nombre + (detectado ? " detecta a " : " no detecta a ") + objetivo.getNombre() + ".");
        return detectado;
    }

    public double getRangoAggro() { return rangoAggro; }
}
