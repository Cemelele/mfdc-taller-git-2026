package minecraft;

/**
 * Entidades que normalmente no atacan al jugador.
 * interactuar() es abstracto: el Cerdo y el Aldeano reaccionan distinto.
 */
public abstract class NoHostil extends Entidad {

    public NoHostil(String nombre, int vida, Hitbox hitboxFisico, String drop) {
        super(nombre, vida, hitboxFisico, drop);
    }

    /** Qué pasa cuando el jugador hace click derecho sobre la entidad. */
    public abstract void interactuar(Jugador jugador);

    /** Comportamiento común de los pasivos: huir al recibir daño. */
    public void huir() {
        System.out.println(nombre + " huye asustado.");
        avanzar(3);
    }
}
