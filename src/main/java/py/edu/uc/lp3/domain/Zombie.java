package py.edu.uc.lp3.domain;

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
        super(nombre, vida, new Hitbox(0.6, 1.95), "Carne podrida", 35);
        this.seQuemaAlSol = true;
    }

    @Override
    public String accionAtaque(Jugador objetivo) {
        return "Zombie golpea cuerpo a cuerpo a " + objetivo.getNombre()
                + ". " + objetivo.recibirDanio(3);
    }

    public String amanecer() {
        if (!seQuemaAlSol) {
            return "El Zombie aguanta el sol.";
        }
        return "Zombie se prende fuego con la luz del sol. " + recibirDanio(1);
    }

    public boolean isSeQuemaAlSol() { return seQuemaAlSol; }
}
