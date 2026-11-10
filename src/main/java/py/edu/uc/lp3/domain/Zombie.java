package py.edu.uc.lp3.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("zombie")
public class Zombie extends Hostil {

    private boolean seQuemaAlSol;

    /** Constructor simple: delega en el sobrecargado con los valores por defecto. */
    public Zombie() {
        this("Zombie", 20);
    }

    /** Constructor sobrecargado: misma clase, otra lista de argumentos (vida inicial). */
    public Zombie(int vida) {
        this("Zombie", vida);
    }

    /** Constructor completo: el que resuelve el estado y llama a super. */
    public Zombie(String nombre, int vida) {
        super(nombre, vida, new Hitbox(0.6, 1.95), "Carne podrida",
                "texturas/zombie.png", "gruñido", 0.23, 35, 3);
        this.seQuemaAlSol = true;
    }

    @Override
    public String accionAtaque(Jugador objetivo) {
        return "Zombie golpea cuerpo a cuerpo a " + objetivo.getNombre()
                + ". " + objetivo.recibirDanio(getDanioAtaque());
    }

    public String amanecer() {
        if (!seQuemaAlSol) {
            return "El Zombie aguanta el sol.";
        }
        return "Zombie se prende fuego con la luz del sol. " + recibirDanio(1);
    }

    public boolean isSeQuemaAlSol() { return seQuemaAlSol; }
}
