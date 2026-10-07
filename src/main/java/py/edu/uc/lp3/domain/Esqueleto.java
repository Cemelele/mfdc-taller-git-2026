package py.edu.uc.lp3.domain;

public class Esqueleto extends Hostil {

    private int flechas;

    public Esqueleto() {
        super("Esqueleto", 20, new Hitbox(0.6, 1.99), "Huesos y flechas", 16);
        this.flechas = 10;
    }

    @Override
    public String accionAtaque(Jugador objetivo) {
        if (flechas <= 0) {
            return "Esqueleto se quedo sin flechas, no puede atacar.";
        }
        flechas--;
        return "Esqueleto dispara una flecha a " + objetivo.getNombre()
                + ". " + objetivo.recibirDanio(2) + " (quedan " + flechas + " flechas)";
    }

    public int getFlechas() { return flechas; }
}
