package minecraft;

public class Enderman extends Hostil {

    private String bloqueEnMano;

    public Enderman() {
        super("Enderman", 40, new Hitbox(0.6, 2.9), "Perla de ender", 64);
        this.bloqueEnMano = null;
    }

    @Override
    public void accionAtaque(Jugador objetivo) {
        teletransportarse();
        System.out.println("Enderman ataca a " + objetivo.getNombre() + " (7 de daño).");
        objetivo.recibirDanio(7);
    }

    public void teletransportarse() {
        this.x += Math.random() * 32 - 16;
        this.z += Math.random() * 32 - 16;
        System.out.println("Enderman se teletransporta.");
    }

    public void tomarBloque(String bloque) {
        this.bloqueEnMano = bloque;
        System.out.println("Enderman levanta un bloque de " + bloque + ".");
    }

    public String getBloqueEnMano() { return bloqueEnMano; }
}
