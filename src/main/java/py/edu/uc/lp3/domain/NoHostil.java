package py.edu.uc.lp3.domain;

/**
 * Entidades que normalmente no atacan al jugador.
 *
 * interactuar() es abstracto: el Cerdo y el Aldeano reaccionan distinto.
 * Es la segunda realizacion del metodo abstracto de Entidad.
 */
public abstract class NoHostil extends Entidad {

    protected NoHostil(String nombre, int vida, Hitbox hitboxFisico, String drop) {
        super(nombre, vida, hitboxFisico, drop);
    }

    /**
     * Un pasivo se activa interactuando. El mensaje comun lo arma esta
     * clase; lo que pase al interactuar es de cada hija.
     */
    @Override
    public final String comportamiento(Jugador objetivo) {
        return estaMuerto()
                ? getNombre() + " ya murio."
                : interactuar(objetivo);
    }

    /** Que pasa cuando el jugador hace click derecho sobre la entidad. */
    public abstract String interactuar(Jugador jugador);

    /** Comportamiento comun de los pasivos: huir al recibir dano. */
    public String huir() {
        return getNombre() + " huye asustado. " + avanzar(3);
    }
}
