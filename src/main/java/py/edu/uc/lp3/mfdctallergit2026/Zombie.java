package minecraft;

public class Zombie extends Hostil {

    private boolean seQuemaAlSol;

    public Zombie() {
        super("Zombie", 20, new Hitbox(0.6, 1.95), "Carne podrida", 35);
        this.seQuemaAlSol = true;
    }

    @Override
    public void accionAtaque(Jugador objetivo) {
        System.out.println("Zombie golpea cuerpo a cuerpo a " + objetivo.getNombre() + " (3 de daño).");
        objetivo.recibirDanio(3);
    }

    public void amanecer() {
        if (seQuemaAlSol) {
            System.out.println("Zombie se prende fuego con la luz del sol.");
            recibirDanio(1);
        }
    }
}
