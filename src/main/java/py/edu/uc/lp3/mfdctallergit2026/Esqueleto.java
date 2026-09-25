package minecraft;

public class Esqueleto extends Hostil {

    private int flechas;

    public Esqueleto() {
        super("Esqueleto", 20, new Hitbox(0.6, 1.99), "Huesos y flechas", 16);
        this.flechas = 10;
    }

    @Override
    public void accionAtaque(Jugador objetivo) {
        if (flechas <= 0) {
            System.out.println("Esqueleto se quedó sin flechas.");
            return;
        }
        flechas--;
        System.out.println("Esqueleto dispara una flecha a " + objetivo.getNombre() + " (2 de daño).");
        objetivo.recibirDanio(2);
    }

    public int getFlechas() { return flechas; }
}
