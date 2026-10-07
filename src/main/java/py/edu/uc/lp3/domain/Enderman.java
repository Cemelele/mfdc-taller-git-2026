package py.edu.uc.lp3.domain;

public class Enderman extends Hostil {

    private String bloqueEnMano;

    public Enderman() {
        super("Enderman", 40, new Hitbox(0.6, 2.9), "Perla de ender", 64);
        this.bloqueEnMano = null;
    }

    @Override
    public String accionAtaque(Jugador objetivo) {
        return teletransportarse() + " Luego ataca a " + objetivo.getNombre()
                + ". " + objetivo.recibirDanio(7);
    }

    /** El Enderman cambia de posicion; por eso usa desplazar() del padre. */
    public String teletransportarse() {
        return getNombre() + " se teletransporta. "
                + desplazar(Math.random() * 32 - 16, 0, Math.random() * 32 - 16);
    }

    public String tomarBloque(String bloque) {
        if (bloque == null || bloque.isBlank()) {
            throw new IllegalArgumentException("No se puede tomar un bloque vacio");
        }
        this.bloqueEnMano = bloque;
        return "Enderman levanta un bloque de " + bloque + ".";
    }

    public String getBloqueEnMano() { return bloqueEnMano; }
}
